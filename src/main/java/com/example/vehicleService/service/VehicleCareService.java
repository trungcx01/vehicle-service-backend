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
    VehicleCare getById(Integer id);
    Page<VehicleCare> getAllPagination(Pageable pageable);
    VehicleCare save(VehicleCareDTO vehicleCareDTO, MultipartFile image);
    void delete(Integer id);
    List<VehicleCare> getByShop(Integer shopId);
    Page<VehicleCare> getByShop(Pageable pageable);
    List<VehicleCare> search(String name, String district, Integer priceFrom, Integer priceTo);
    Page<VehicleCare> searchVehicleCares(String searchTerm, Pageable pageable);
    Page<VehicleCare> searchVehicleCaresByShop(String searchTerm, Pageable pageable);
}
