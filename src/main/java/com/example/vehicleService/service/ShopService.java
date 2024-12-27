package com.example.vehicleService.service;

import com.example.vehicleService.dto.CustomerDTO;
import com.example.vehicleService.dto.ShopDTO;
import com.example.vehicleService.entity.Customer;
import com.example.vehicleService.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShopService {
    Shop getById(Long id);
    Page<Shop> getAllPagination(Pageable pageable);
    Shop save(ShopDTO shopDTO);
    void delete(Long id);
    List<Shop> findTop6ByOrderByRatingDesc();
    Shop getCurrentShop();
    List<Shop> search(String name, String district);
}
