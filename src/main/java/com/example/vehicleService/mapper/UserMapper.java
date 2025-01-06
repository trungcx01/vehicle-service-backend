package com.example.vehicleService.mapper;

import com.example.vehicleService.dto.UserRegisterDTO;
import com.example.vehicleService.entity.User;
import com.example.vehicleService.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring", uses = {UserRepository.class})
public interface UserMapper extends EntityMapper<UserRegisterDTO, User>{
    UserRegisterDTO toDto(User user);
    User toEntity(UserRegisterDTO userRegisterDTO);

    default User fromId(Integer  id, @Context UserRepository userRepository) {
        if (id == null){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return userRepository.findByUsername(authentication.getName()).orElse(null);
        }
        return userRepository.findById(id).orElse(null);
    }
}
