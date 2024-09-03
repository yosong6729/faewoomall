package com.example.faewoomall.repository;

import com.example.faewoomall.domain.Order;
import com.example.faewoomall.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByItemNameContaining(String keyword, Pageable pageable);

    Page<Order> findByUserEquals(User user, Pageable pageable);
}
