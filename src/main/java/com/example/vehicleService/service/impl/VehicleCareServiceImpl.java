package com.example.vehicleService.service.impl;

import com.example.vehicleService.dto.VehicleCareDTO;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.VehicleCare;
import com.example.vehicleService.mapper.VehicleCareMapper;
import com.example.vehicleService.mapper.VehicleCareMapperImpl;
import com.example.vehicleService.repository.ShopRepository;
import com.example.vehicleService.repository.VehicleCareRepository;
import com.example.vehicleService.service.VehicleCareService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleCareServiceImpl implements VehicleCareService {
    private final ShopRepository shopRepository;
    private final VehicleCareMapper vehicleCareMapper;
    private final VehicleCareRepository vehicleCareRepository;


    public VehicleCareServiceImpl(ShopRepository shopRepository, VehicleCareMapper vehicleCareMapper, VehicleCareRepository vehicleCareRepository) {
        this.shopRepository = shopRepository;
        this.vehicleCareMapper = vehicleCareMapper;
        this.vehicleCareRepository = vehicleCareRepository;
    }

    @Override
    public VehicleCare getById(Long id) {
        return vehicleCareRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Vehicle Care!")
        );
    }

    @Override
    public Page<VehicleCare> getAllPagination(Pageable pageable) {
        return vehicleCareRepository.findAll(pageable);
    }

    @Override
    public VehicleCare save(VehicleCareDTO vehicleCareDTO) {
        VehicleCare vehicleCare = vehicleCareMapper.toEntity(vehicleCareDTO, shopRepository);
        return vehicleCareRepository.save(vehicleCare);
    }

    @Override
    public void delete(Long id) {
        VehicleCare vehicleCare = vehicleCareRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found Vehicle Care!")
        );
        vehicleCareRepository.delete(vehicleCare);
    }

    @Override
    public List<VehicleCare> getByShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElseThrow(
                () -> new EntityNotFoundException("Not found Shop!")
        );
        return vehicleCareRepository.findByShop(shop);
    }

    @Override
    public List<VehicleCare> getByShop() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Shop shop = shopRepository.findByUserUsername(username);
        return vehicleCareRepository.findByShop(shop);
    }

    @Override
    public List<VehicleCare> search(String name, Long start, Long end) {
        return vehicleCareRepository.findByNameContainingIgnoreCaseAndPriceIsGreaterThanEqualAndPriceIsLessThanEqual(name, start, end);
    }
}
