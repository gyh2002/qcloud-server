package com.orange.qcloud.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RenameFileRequest {
    private Long id;
    private Long parentId;
    private String newName;
}
