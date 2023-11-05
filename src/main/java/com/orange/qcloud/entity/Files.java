package com.orange.qcloud.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "Files")
@Table(name = "files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Files {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isFolder;

    @Column(nullable = false)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName="id", nullable=false)
    private Users user;

    @Column(nullable = false)
    private boolean isRoot;

    @Column(nullable = false)
    private String hash;

    @Column(nullable = false)
    private Long versionNo;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private List<Files> children;

    public void addChild(Files child) {
        children.add(child);
    }

    public void removeChild(Files child) {
        children.remove(child);
    }
}
