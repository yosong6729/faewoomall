package com.example.faewoomall.service;

import com.example.faewoomall.domain.Item;
import com.example.faewoomall.domain.User;
import com.example.faewoomall.domain.WishList;
import com.example.faewoomall.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {

    public final WishListRepository wishListRepository;

    public void saveWishList(Item item, User user) {
        WishList wishList = new WishList();
        wishList.setUser(user);
        wishList.setItem(item);

        wishListRepository.save(wishList);
    }

    public void deleteWishList(Long wishListId) {

        wishListRepository.deleteById(wishListId);
    }

    public Page<WishList> findPagedWishList(int page) {
        List<Sort.Order> sort = new ArrayList<>();
        sort.add(Sort.Order.by("createDate"));

        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by(sort));

        return wishListRepository.findAll(pageRequest);
    }

}
