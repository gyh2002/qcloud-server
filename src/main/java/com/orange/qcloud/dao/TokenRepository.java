package com.orange.qcloud.dao;

import com.orange.qcloud.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = """
        select t from Token t inner join Users u\s
        on t.user.id = u.id\s
        where u.id = :id and (t.expired = false or t.revoked = false) and t.isTemp = true\s
        """)
    List<Token> findAllValidTempTokenByUser(Long id);

    Optional<Token> findByToken(String token);

    @Query(value = """
        select t from Token t inner join Users u\s
        on t.user.id = u.id\s
        where u.id = :id and (t.expired = false or t.revoked = false) and t.isTemp = false\s
        """)
    List<Token> findAllValidForeverTokenByUser(Long id);
}
