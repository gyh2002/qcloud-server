package com.orange.qcloud.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.qcloud.dao.FilesRepository;
import com.orange.qcloud.entity.*;
import com.orange.qcloud.response.CmdAuthenticationResponse;
import com.orange.qcloud.utils.FileUtils;
import com.orange.qcloud.utils.JwtService;
import com.orange.qcloud.dao.TokenRepository;
import com.orange.qcloud.dao.UsersRepository;
import com.orange.qcloud.request.LoginRequest;
import com.orange.qcloud.request.RegisterRequest;
import com.orange.qcloud.response.AuthenticationResponse;
import com.orange.qcloud.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    @Value("${qcloud.file.root-path}")
    private String ROOT_PATH;

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final FilesRepository filesRepository;

    @Override
    public List<Users> findAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Users findUserByEmail(String email) {
        return usersRepository.findUsersByEmail(email);
    }

    @Override
    public AuthenticationResponse register(RegisterRequest registerReq) {
        var user = Users.builder()
                .username(registerReq.getUsername())
                .email(registerReq.getEmail())
                .password(passwordEncoder.encode(registerReq.getPassword()))
                .role(Role.USER)
                .rootPath(getNewRootPath())
                .build();
        var savedUser = usersRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserTempToken(savedUser, jwtToken);
        createUserRootFolderAndWelcomeFile(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginReq) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReq.getEmail(),
                        loginReq.getPassword()
                )
        );
        var user = usersRepository.findByEmail(loginReq.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTempTokens(user);
        saveUserTempToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public CmdAuthenticationResponse cmdLogin(LoginRequest loginReq) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReq.getEmail(),
                        loginReq.getPassword()
                )
        );
        var user = usersRepository.findByEmail(loginReq.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateLongToken(user);
        revokeAllUserForeverTokens(user);
        saveUserForeverToken(user, jwtToken);
        return CmdAuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = usersRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTempTokens(user);
                saveUserTempToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    private void saveUserTempToken(Users user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .isTemp(true)
                .build();
        tokenRepository.save(token);
    }

    private void saveUserForeverToken(Users user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .isTemp(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTempTokens(Users user) {
        var validUserTokens = tokenRepository.findAllValidTempTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void revokeAllUserForeverTokens(Users user) {
        var validUserTokens = tokenRepository.findAllValidForeverTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private String getNewRootPath() {
        return UUID.randomUUID().toString();
    }

    private void createUserRootFolderAndWelcomeFile(Users user) {
        String folderName = user.getRootPath();
        String rootFolderFullPath = Paths.get(ROOT_PATH, folderName).toString();
        String welFileFullPath = Paths.get(ROOT_PATH, folderName, "welcome.md").toString();
        FileUtils.createDirectory(rootFolderFullPath);
        FileUtils.createFile(welFileFullPath);
        Files rootFolder = Files.builder()
                .name(folderName)
                .isFolder(true)
                .path(folderName)
                .user(user)
                .isRoot(true)
                .hash("")
                .versionNo(0L)
                .children(new ArrayList<>())
                .build();
        Files welcomeFile = Files.builder()
                .name("welcome.md")
                .isFolder(false)
                .path(Paths.get(rootFolder.getPath(),"welcome.md").toString())
                .user(user)
                .isRoot(false)
                .hash(FileUtils.getFileHash(welFileFullPath))
                .versionNo(0L)
                .build();
        rootFolder.addChild(welcomeFile);
        filesRepository.save(rootFolder);
    }
}
