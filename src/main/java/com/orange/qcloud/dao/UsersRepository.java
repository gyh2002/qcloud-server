package com.orange.qcloud.dao;

import com.orange.qcloud.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findUsersByEmail(String email);
    Optional<Users> findByEmail(String email);
    @Query("SELECT MAX(u.id) FROM Users u")
    Long findTopId();
    @Query("select currval('users_seq')")
    Long getCurrValUsersSeq();
}
