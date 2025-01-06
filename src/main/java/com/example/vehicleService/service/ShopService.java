package com.example.vehicleService.service;

import com.example.vehicleService.dto.CustomerDTO;
import com.example.vehicleService.dto.ShopDTO;
import com.example.vehicleService.entity.Customer;
import com.example.vehicleService.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ShopService {
    Shop getById(Integer id);
    Page<Shop> getAllPagination(Pageable pageable);
    Shop save(ShopDTO shopDTO, MultipartFile coverImage);
    void delete(Integer id);
    List<Shop> findTop6ByOrderByRatingDesc();
    Shop getCurrentShop();
    List<Shop> search(String name, String district, Integer rating);
    List<Shop> findTop10Revenue();
    Page<Shop> searchShops(String searchTerm, Pageable pageable);
}
