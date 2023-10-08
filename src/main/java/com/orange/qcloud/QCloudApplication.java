package com.orange.qcloud;

import com.orange.qcloud.dao.FilesRepository;
import com.orange.qcloud.dao.UsersRepository;
import com.orange.qcloud.entity.Files;
import com.orange.qcloud.entity.Role;
import com.orange.qcloud.entity.Users;
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
            UsersRepository usersRepository,
            FilesRepository filesRepository
    ) {
        return args -> {
            Users u0 = Users.builder()
                    .username("admin")
                    .email("admin@qilixiang.cc")
                    .password("$2a$10$NL2yicN6NtYxCPKXDgr7R.1CA23Q/mMd4yC7Y15AcuSm8nGklcm.C")
                    .role(Role.ADMIN)
                    .rootPath("1")
                    .build();
            usersRepository.save(u0);

            Users u1 = Users.builder()
                    .username("Ge YunHao")
                    .email("qilixiangcc@proton.me")
                    .password("$2a$10$NL2yicN6NtYxCPKXDgr7R.1CA23Q/mMd4yC7Y15AcuSm8nGklcm.C")
                    .role(Role.USER)
                    .rootPath("" + (usersRepository.getCurrValUsersSeq() + 1))
                    .build();
            usersRepository.save(u1);

            Files f0 = Files.builder()
                    .name("qcloud")
                    .isFolder(true)
                    .deleted(false)
                    .user(u1)
                    .path("\\")
                    .parent(null)
                    .build();
            filesRepository.save(f0);

            Files f1 = Files.builder()
                    .name("geyunhao")
                    .isFolder(true)
                    .deleted(false)
                    .user(u1)
                    .path("\\geyunhao")
                    .parent(f0)
                    .build();
            filesRepository.save(f1);
        };
    }

}
