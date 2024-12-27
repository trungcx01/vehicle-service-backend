package com.example.vehicleService.service.impl;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.cloudinary.Cloudinary;
import com.example.vehicleService.dto.EmailDetail;
import com.example.vehicleService.dto.ForgotPasswordDTO;
import com.example.vehicleService.dto.LoginDTO;
import com.example.vehicleService.dto.UserRegisterDTO;
import com.example.vehicleService.entity.Role;
import com.example.vehicleService.entity.User;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.mapper.UserMapper;
import com.example.vehicleService.repository.RoleRepository;
import com.example.vehicleService.repository.UserRepository;
import com.example.vehicleService.security.JwtTokenProvider;
import com.example.vehicleService.service.AuthService;
import com.example.vehicleService.service.CloudinaryService;
import com.example.vehicleService.service.MailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service

public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, UserMapper userMapper, RoleRepository roleRepository, UserRepository userRepository, Cloudinary cloudinary, CloudinaryService cloudinaryService, PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
        this.cloudinaryService = cloudinaryService;
        this.encoder = encoder;
    }

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getUsernameOrEmail(), loginDTO.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public User signUp(UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsByUsernameOrEmail(userRegisterDTO.getUsername(), userRegisterDTO.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "User exists!");
        }
        User user = userMapper.toEntity(userRegisterDTO);
        String imageUrl = cloudinary.url().generate("anonymous-user_gbpozf");
        user.setImageUrl(imageUrl);
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        user.setActivated(false);
        user.setActivationKey(String.valueOf(new Random().nextInt(999999 - 100000 + 1) + 100000));
//        Role role = roleRepository.findByName("USER").orElseThrow(
//                () -> new EntityNotFoundException("Role USER not found")
//        );
//        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    @Override
    public User resetActivationKey() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).map(
                user -> {
                    user.setActivationKey(String.valueOf(new Random().nextInt(999999 - 100000 + 1) + 100000));
                    return userRepository.save(user);
                }
        ).orElseThrow(() -> new EntityNotFoundException("Not found user!"));
    }

    @Override
    public User activateUser(String key) {
        return userRepository.findByActivationKey(key)
                .map(user -> {
                    user.setActivated(true);
                    user.setActivationKey(null);
                    return userRepository.save(user);
                }).orElse(null);
    }


    @Override
    public User sendResetKey(String email) {
        return userRepository.findByEmail(email).map(
                user -> {
                    user.setResetKey(String.valueOf(new Random().nextInt(999999 - 100000 + 1) + 100000));
                    return userRepository.save(user);
                }
        ).orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    @Override
    public User resetPassword(ForgotPasswordDTO forgotPasswordDTO) {
        return userRepository.findByResetKey(forgotPasswordDTO.getResetKey()).map(
                user -> {
                    user.setResetDate(LocalDateTime.now());
                    user.setResetKey(null);
                    user.setPassword(passwordEncoder.encode(forgotPasswordDTO.getNewPassword()));
                    return userRepository.save(user);
                }
        ).orElseThrow(() -> new EntityNotFoundException("User not found!"));
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Not found user!"));
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new IllegalArgumentException("Old password cannot be null or empty");
        }

        User user = getCurrentUser();
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedNewPassword);
            userRepository.save(user);
        } else {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Old password doesn't match");
        }
    }

}
