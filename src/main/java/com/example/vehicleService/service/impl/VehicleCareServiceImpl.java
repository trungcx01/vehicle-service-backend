package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.VehicleCareDTO;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.VehicleCare;
import com.example.vehicleService.mapper.VehicleCareMapper;
import com.example.vehicleService.mapper.VehicleCareMapperImpl;
import com.example.vehicleService.repository.ShopRepository;
import com.example.vehicleService.repository.VehicleCareRepository;
import com.example.vehicleService.service.CloudinaryService;
import com.example.vehicleService.service.VehicleCareService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public class VehicleCareServiceImpl implements VehicleCareService {
    private final ShopRepository shopRepository;
    private final VehicleCareMapper vehicleCareMapper;
    private final VehicleCareRepository vehicleCareRepository;
    private final CloudinaryService cloudinaryService;


    public VehicleCareServiceImpl(ShopRepository shopRepository, VehicleCareMapper vehicleCareMapper, VehicleCareRepository vehicleCareRepository, CloudinaryService cloudinaryService) {
        this.shopRepository = shopRepository;
        this.vehicleCareMapper = vehicleCareMapper;
        this.vehicleCareRepository = vehicleCareRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public VehicleCare getById(Integer id) {
        return vehicleCareRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Vehicle Care!")
        );
    }

    @Override
    public Page<VehicleCare> getAllPagination(Pageable pageable) {
        return vehicleCareRepository.findAll(pageable);
    }

    @Override
    public VehicleCare save(VehicleCareDTO vehicleCareDTO, MultipartFile image) {
        VehicleCare vehicleCare = vehicleCareMapper.toEntity(vehicleCareDTO, shopRepository);
       if (image != null){
           vehicleCare = vehicleCareRepository.save(vehicleCare);
           Map img =  cloudinaryService.upload(image, "shop/" + vehicleCare.getShop() + "/" + vehicleCare.getId());
           vehicleCare.setImageUrl(img.get("secure_url").toString());
       } else{
           VehicleCare care = vehicleCareRepository.findById(vehicleCareDTO.getId()).orElseThrow(
                   () -> new EntityNotFoundException("Not found Vehicle Care!")
           );
           vehicleCare.setImageUrl(care.getImageUrl());
       }
        return vehicleCareRepository.save(vehicleCare);
    }

    @Override
    public void delete(Integer id) {
        VehicleCare vehicleCare = vehicleCareRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Vehicle Care!")
        );
        vehicleCare.setDeleted(true);
        vehicleCareRepository.save(vehicleCare);
    }

    @Override
    public List<VehicleCare> getByShop(Integer shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(
                () -> new EntityNotFoundException("Not found Shop!")
        );
        return vehicleCareRepository.findByShop(shop);
    }

    @Override
    public Page<VehicleCare> getByShop(Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);
        return vehicleCareRepository.findByShop(shop, pageable);
    }

    @Override
    public List<VehicleCare> search(String name, String district, Integer priceFrom, Integer priceTo) {
        return vehicleCareRepository.searchVehicleCare(name, district, priceFrom, priceTo);
    }

    @Override
    public Page<VehicleCare> searchVehicleCares(String searchTerm, Pageable pageable) {
        return vehicleCareRepository.searchVehicleCares(searchTerm, pageable);
    }

    @Override
    public Page<VehicleCare> searchVehicleCaresByShop(String searchTerm, Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);
        return vehicleCareRepository.searchVehicleCaresByShop(searchTerm, shop.getId(), pageable);
    }

    @Override
    public List<VehicleCare> getByShopAndAvailable(Integer shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(
                () -> new EntityNotFoundException("Not found Shop!")
        );
        return vehicleCareRepository.findByAvailableIsTrueAndShop(shop);
    }
}
