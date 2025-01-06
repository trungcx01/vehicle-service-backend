package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.ShopDTO;
import com.example.vehicleService.entity.Role;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.mapper.ShopMapper;
import com.example.vehicleService.repository.RoleRepository;
import com.example.vehicleService.repository.ShopRepository;
import com.example.vehicleService.repository.UserRepository;
import com.example.vehicleService.service.CloudinaryService;
import com.example.vehicleService.service.ShopService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ShopServiceImpl implements ShopService{
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CloudinaryService cloudinaryService;
    private final ShopMapper shopMapper;

    public ShopServiceImpl(ShopRepository shopRepository, UserRepository userRepository, RoleRepository roleRepository, CloudinaryService cloudinaryService, ShopMapper shopMapper) {
        this.shopRepository = shopRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cloudinaryService = cloudinaryService;
        this.shopMapper = shopMapper;
    }

    @Override
    public Shop getById(Integer id) {
        return shopRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Shop!")
        );
    }

    @Override
    public Page<Shop> getAllPagination(Pageable pageable) {
        return shopRepository.findAll(pageable);
    }

    @Override
    public Shop save(ShopDTO shopDTO, MultipartFile coverImage) {
        Shop shop = shopMapper.toEntity(shopDTO, userRepository);

        if (shopDTO.getId() == null) {
            Role role = roleRepository.findByName("SHOP").orElseThrow(
                    () -> new EntityNotFoundException("Not found Role SHOP!")
            );
            shop.getUser().setRoles(Set.of(role));

                Map cover = cloudinaryService.upload(coverImage, "shops");
                shop.setCoverImage(cover.get("secure_url").toString());

        } else {
           if (coverImage != null){
                Map cover = cloudinaryService.upload(coverImage, "shops");
                shop.setCoverImage(cover.get("secure_url").toString());
            }
          else{
               Shop shop1 = shopRepository.findById(shopDTO.getId()).orElseThrow(
                       () -> new EntityNotFoundException("Not found shop!")
               );
               shop.setCoverImage(shop1.getCoverImage());
           }
        }

        return shopRepository.save(shop);
    }


    @Override
    public void delete(Integer id) {
        Shop shop = shopRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Shop!")
        );
        shop.setDeleted(true);
        shop.getUser().setLocked(true);
        shopRepository.save(shop);
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
    public List<Shop> search(String name, String district, Integer rating) {
        return shopRepository.searchShop(name, district, Double.parseDouble(String.valueOf(rating)));
    }

    @Override
    public List<Shop> findTop10Revenue() {
        return  shopRepository.findTop10Revenue();
    }

    @Override
    public Page<Shop> searchShops(String searchTerm, Pageable pageable) {
        return shopRepository.searchShopsNative(searchTerm, pageable);
    }
}
