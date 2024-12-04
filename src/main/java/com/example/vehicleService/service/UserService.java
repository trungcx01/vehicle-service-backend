package com.example.vehicleService.service;

import com.example.vehicleService.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User updateAvatar(MultipartFile image, Long userId);
}
