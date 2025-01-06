package com.example.vehicleService.service;

import com.example.vehicleService.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface UserService {
    User updateAvatar(MultipartFile image, Integer userId);

    Integer getUserByDate(LocalDate date);

    void lockAccount(Integer userId);
    void unlockAccount(Integer userId);
    long count();
}
