package com.orange.qcloud.controller;

import com.orange.qcloud.common.ApiResponse;
import com.orange.qcloud.dto.FilesDTO;
import com.orange.qcloud.dto.UsersDto;
import com.orange.qcloud.request.LoginRequest;
import com.orange.qcloud.request.RegisterRequest;
import com.orange.qcloud.entity.Users;
import com.orange.qcloud.response.AuthenticationResponse;
import com.orange.qcloud.response.CmdAuthenticationResponse;
import com.orange.qcloud.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @GetMapping("/get-all")
    public ApiResponse<List<Users>> getAllUser() {
        return ApiResponse.success(usersService.findAllUsers());
    }

    @GetMapping("/get-by-email")
    public ApiResponse<UsersDto> getUserByEmail(@RequestParam String email) {
        return ApiResponse.success(usersService.findUserByEmail(email));
    }

    @GetMapping("/get-by-token")
    public ApiResponse<UsersDto> getUserByToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ApiResponse.success(usersService.findUserByEmail(auth.getName()));
    }

    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(@RequestBody RegisterRequest registerReq) {
        return ApiResponse.success(usersService.register(registerReq));
    }

    @GetMapping("/send-code")
    public ApiResponse<String> sendCode(@RequestParam String email) {
        return ApiResponse.success(usersService.sendEmailCode(email));
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody LoginRequest loginReq) {
        return ApiResponse.success(usersService.login(loginReq));
    }

    @PostMapping("/cmd-login")
    public ApiResponse<CmdAuthenticationResponse> cmdLogin(@RequestBody LoginRequest loginReq) {
        return ApiResponse.success(usersService.cmdLogin(loginReq));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        usersService.refreshToken(request, response);
    }
}
