package com.example.faewoomall.repository;

import com.example.faewoomall.domain.Category;
import com.example.faewoomall.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAll(Pageable pageable);

    Page<Item> findByCategory(Category category, Pageable pageable);

    Item findItemById(Long id);

    Page<Item> findByNameContains(String keyword, Pageable pageable);

}
