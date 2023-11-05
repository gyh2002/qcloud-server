package com.orange.qcloud.dao;

import com.orange.qcloud.entity.Files;
import com.orange.qcloud.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<Files, Long> {
    Files getFilesByUserAndIsRoot(Users user, boolean isRoot);
    Files getFilesByUserAndId(Users user, long id);
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Files f JOIN f.children c WHERE f.id = :id AND c.name = :childrenName")
    boolean existsByIdAndChildrenName(Long id, String childrenName);
    @Query(value = "SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM files f WHERE f.id = :id AND f.parent_id = :parentId", nativeQuery = true)
    boolean existsByIdAndParentId(Long id, Long parentId);
}
