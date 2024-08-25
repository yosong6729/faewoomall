package com.example.faewoomall.repository;

import com.example.faewoomall.domain.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    Page<WishList> findAll(Pageable pageable);
}
