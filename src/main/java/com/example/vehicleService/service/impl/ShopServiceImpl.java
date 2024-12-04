package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.ShopDTO;
import com.example.vehicleService.entity.Role;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.mapper.ShopMapper;
import com.example.vehicleService.repository.RoleRepository;
import com.example.vehicleService.repository.ShopRepository;
import com.example.vehicleService.repository.UserRepository;
import com.example.vehicleService.service.ShopService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ShopServiceImpl implements ShopService{
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShopMapper shopMapper;

    public ShopServiceImpl(ShopRepository shopRepository, UserRepository userRepository, RoleRepository roleRepository, ShopMapper shopMapper) {
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.shopMapper = shopMapper;
    }

    @Override
    public Shop getById(Long id) {
        return shopRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Shop!")
        );
    }

    @Override
    public Page<Shop> getAllPagination(Pageable pageable) {
        return shopRepository.findAll(pageable);
    }

    @Override
    public Shop save(ShopDTO shopDTO) {
        Shop shop = shopMapper.toEntity(shopDTO, userRepository);
       if (shopDTO.getId() == null){
           Role role = roleRepository.findByName("SHOP").orElseThrow(
                   () -> new EntityNotFoundException("Not found Role SHOP!")
           );
           shop.getUser().setRoles(Set.of(role));
       }
        return shopRepository.save(shop);
    }

    @Override
    public void delete(Long id) {
        Shop shop = shopRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Shop!")
        );
        shopRepository.delete(shop);
    }

    @Override
    public List<Shop> findTop6ByOrderByRatingDesc() {
        return shopRepository.findTop6ByOrderByRatingDesc();
    }

    @Override
    public Shop getCurrentShop() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);
        return shop;
    }

    @Override
    public List<Shop> searchByName(String name) {
        return shopRepository.findByNameContainingIgnoreCase(name);
    }
}
