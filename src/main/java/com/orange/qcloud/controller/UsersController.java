package com.orange.qcloud.controller;

import com.orange.qcloud.common.ApiResponse;
import com.orange.qcloud.request.LoginRequest;
import com.orange.qcloud.request.RegisterRequest;
import com.orange.qcloud.entity.Users;
import com.orange.qcloud.response.AuthenticationResponse;
import com.orange.qcloud.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<Users> getUserByEmail(@RequestParam String email) {
        return ApiResponse.success(usersService.findUserByEmail(email));
    }

    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(@RequestBody RegisterRequest registerReq) {
        return ApiResponse.success(usersService.register(registerReq));
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody LoginRequest loginReq) {
        return ApiResponse.success(usersService.login(loginReq));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        usersService.refreshToken(request, response);
    }
}
