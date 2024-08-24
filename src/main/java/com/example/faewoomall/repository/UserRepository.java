package com.example.faewoomall.repository;

import com.example.faewoomall.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    User findByEmail(String email);

    Page<User> findAll(Pageable pageable);

    User findUserById(Long userId);

    User findByOauth2Id(String oAuth2Id);

    User findByUserId(String userId);

    Page<User> findByEmailContaining(String keyword, Pageable pageable);
}
