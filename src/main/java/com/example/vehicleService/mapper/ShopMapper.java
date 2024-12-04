package com.example.vehicleService.mapper;

import com.example.vehicleService.dto.ShopDTO;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.exception.BlogAPIException;
import com.example.vehicleService.repository.ShopRepository;
import com.example.vehicleService.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ShopMapper extends EntityMapper<ShopDTO, Shop>{
    @Mapping(source = "userId", target = "user")
    Shop toEntity(ShopDTO dto, @Context UserRepository userRepository);
    @Mapping(source = "user.id", target = "userId")
    ShopDTO toDto(Shop entity);

    default Shop fromId(Long id, @Context ShopRepository shopRepository){
        if (id == null){
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return shopRepository.findByUserUsername(username);
        }
        return shopRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Shop!")
        );
    }
}
