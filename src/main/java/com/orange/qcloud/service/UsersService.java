package com.orange.qcloud.service;

import com.orange.qcloud.request.LoginRequest;
import com.orange.qcloud.request.RegisterRequest;
import com.orange.qcloud.entity.Users;
import com.orange.qcloud.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface UsersService {
    List<Users> findAllUsers();

    Users findUserByEmail(String email);

    AuthenticationResponse register(RegisterRequest registerReq);

    AuthenticationResponse login(LoginRequest loginReq);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
