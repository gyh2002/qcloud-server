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
public class FilesDTO {
    private Long id;
    private String name;
    private boolean isFolder;
    private boolean deleted;
    private boolean isRoot;
    private List<FilesDTO> children;

    public static FilesDTO convertToDTO(Files files) {
        if (files == null) {
            return null;
        }

        FilesDTO dto = new FilesDTO();
        dto.setId(files.getId());
        dto.setName(files.getName());
        dto.setFolder(files.isFolder());
        dto.setRoot(files.isRoot());

        List<Files> children = files.getChildren();
        if (children != null) {
            List<FilesDTO> childrenDTO = new ArrayList<>();
            for (Files child : children) {
                childrenDTO.add(convertToDTO(child));
            }
            dto.setChildren(childrenDTO);
        }

        return dto;
    }
}
