package com.orange.qcloud;

import com.orange.qcloud.request.RegisterRequest;
import com.orange.qcloud.service.FilesService;
import com.orange.qcloud.service.UsersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(QCloudApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            UsersService usersService,
            FilesService filesService
    ) {
        return args -> {
            RegisterRequest user = RegisterRequest.builder()
                    .username("七里香")
                    .email("qilixiangcc@proton.me")
                    .password("123456")
                    .build();
            usersService.register(user);
        };
    }
}
