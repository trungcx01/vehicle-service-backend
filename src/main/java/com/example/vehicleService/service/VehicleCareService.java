package com.example.vehicleService.service;

import com.example.vehicleService.dto.ShopDTO;
import com.example.vehicleService.dto.VehicleCareDTO;
import com.example.vehicleService.entity.Shop;
import com.example.vehicleService.entity.VehicleCare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VehicleCareService {
    VehicleCare getById(Long id);
    Page<VehicleCare> getAllPagination(Pageable pageable);
    VehicleCare save(VehicleCareDTO vehicleCareDTO, MultipartFile image);
    void delete(Long id);
    List<VehicleCare> getByShop(Long shopId);
    List<VehicleCare> getByShop();
    List<VehicleCare> search(String name, Long start, Long end);
}
