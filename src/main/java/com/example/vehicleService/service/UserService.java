package com.example.vehicleService.service;

import com.example.vehicleService.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User updateAvatar(MultipartFile image, Long userId);

    Long getUserByMonth(Long month, Long year);

    void lockAccount(Long userId);
    void unlockAccount(Long userId);
}
