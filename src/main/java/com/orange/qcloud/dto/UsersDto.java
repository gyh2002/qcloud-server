package com.orange.qcloud.dto;

import com.orange.qcloud.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto {
    private Long id;

    private String username;

    private String email;

    private Role role;

    private String rootPath;
}
