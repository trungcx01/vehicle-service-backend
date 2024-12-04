 package com.example.vehicleService.controller;

 import com.example.vehicleService.service.UserService;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.web.multipart.MultipartFile;

 @RestController
 @RequestMapping("api/users")
 public class UserController {
     private final UserService userService;

     public UserController(UserService userService) {
         this.userService = userService;
     }

     @PutMapping("/update-avatar")
     public ResponseEntity<?> updateAvatar(@RequestParam("avatar") MultipartFile avatar){
         return ResponseEntity.ok(userService.updateAvatar(avatar, null));
     }
 }
