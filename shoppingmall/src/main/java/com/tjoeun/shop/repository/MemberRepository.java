package com.tjoeun.shop.repository;

import com.tjoeun.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);
    
    @Query("select member.id " +
           "from Member member " +
           "where member.email = :username ")
    Long findIdByUsername(@Param("username") String username);
    
    void deleteById(@Param("id") Long id);

}