package com.example.faewoomall.service;

import com.example.faewoomall.domain.User;
import com.example.faewoomall.dto.EditUserDTO;
import com.example.faewoomall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAllUser() {

        return userRepository.findAll();
    }

    public User findByOAuth2Id(String oAuth2Id) {
        return userRepository.findByOauth2Id(oAuth2Id);
    }

    public User findById(Long userId) {
        return userRepository.findUserById(userId);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Page<User> getPagingUsers(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("zipcode"));

        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(sorts));

        return userRepository.findAll(pageRequest);
    }

    public void deleteUser(List<String> userIdList) {

        for (String userId : userIdList) {
            userRepository.deleteById(Long.valueOf(userId));
        }
    }

    @Transactional
    public void editUser(Long userId, EditUserDTO editUserDTO) {
        User user = userRepository.findUserById(userId);
        user.setUserId(editUserDTO.getUserId());
        user.setEmail(editUserDTO.getNickName());
        user.setZipcode(editUserDTO.getZipcode());
        user.setStreetAdr(editUserDTO.getStreetAdr());
        user.setDetailAdr(editUserDTO.getDetailAdr());
    }


    @Transactional
    public void editUserRole(List<String> userIdList, String role) {

        if (role.equals("ROLE_USER")) {
            for (String userId : userIdList) {
                User user = userRepository.findUserById(Long.valueOf(userId));
                user.setRole("ROLE_USER");
            }
        } else if (role.equals("ROLE_ADMIN")) {
            for (String userId : userIdList) {
                User user = userRepository.findUserById(Long.valueOf(userId));
                user.setRole("ROLE_ADMIN");
            }
        }
    }

    public Page<User> searchingUserList(String keyword, int page) {
        List<Sort.Order> sort = new ArrayList<>();
        sort.add(Sort.Order.by("createDate"));

        PageRequest pageRequest = PageRequest.of(page, 8, Sort.by(sort));

        return userRepository.findByEmailContaining(keyword, pageRequest);
    }

}
