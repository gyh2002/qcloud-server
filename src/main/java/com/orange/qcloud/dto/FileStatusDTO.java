package com.orange.qcloud.dto;

import com.orange.qcloud.entity.Files;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileStatusDTO {
    private Long id;
    private String name;
    private boolean isFolder;
    private String hash;
    private Long versionNo;
    private List<FileStatusDTO> children;

    public static FileStatusDTO convertToDTO(Files files) {
        if (files == null)
            return null;
        List<Files> children = files.getChildren();
        List<FileStatusDTO> chi = new ArrayList<>();
        if (children != null) {
            for (Files f : children) {
                chi.add(convertToDTO(f));
            }
        }
        return FileStatusDTO.builder()
                .id(files.getId())
                .name(files.getName())
                .isFolder(files.isFolder())
                .hash(files.getHash())
                .versionNo(files.getVersionNo())
                .children(chi)
                .build();
    }
}
