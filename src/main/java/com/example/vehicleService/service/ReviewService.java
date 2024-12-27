package com.example.vehicleService.service;

import com.example.vehicleService.dto.ReviewDTO;
import com.example.vehicleService.dto.ShopDTO;
import com.example.vehicleService.entity.Review;
import com.example.vehicleService.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReviewService {
    Review getById(Long id);
    Page<Review> getAllPagination(Pageable pageable);
    Review save(ReviewDTO reviewDTO, MultipartFile image);
    void delete(Long id);
    List<Review> findTop8ByOrderByCreatedAtDesc();
    Review findByAppointmentId(Long id);
    Review findByProposalId(Long id);
    List<Review> findByShop(Long shopId);
}
