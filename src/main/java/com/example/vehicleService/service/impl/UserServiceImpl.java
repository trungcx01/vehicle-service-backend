package com.example.vehicleService.service.impl;

import com.example.vehicleService.entity.User;
import com.example.vehicleService.repository.UserRepository;
import com.example.vehicleService.service.CloudinaryService;
import com.example.vehicleService.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public UserServiceImpl(UserRepository userRepository, CloudinaryService cloudinaryService) {
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public User updateAvatar(MultipartFile image, Long userId) {
        Map avatar =  cloudinaryService.upload(image, "users");
        User user;
        if (userId != null){
            user = userRepository.findById(userId).orElseThrow(
                    () -> new EntityNotFoundException("Not Found user!")
            );

        }else{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            user = userRepository.findByUsername(username).orElseThrow(
                    () -> new EntityNotFoundException("Not found User!")
            );
        }
        user.setImageUrl(avatar.get("secure_url").toString());
        return userRepository.save(user);
    }

    @Override
    public Long getUserByMonth(Long month, Long year) {
        return userRepository.getUserByMonth(month, year);
    }

    @Override
    public void lockAccount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Not found user!")
        );
        user.setLocked(true);
        userRepository.save(user);
    }

    @Override
    public void unlockAccount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Not found user!")
        );
        user.setLocked(false);
        userRepository.save(user);
    }
}
