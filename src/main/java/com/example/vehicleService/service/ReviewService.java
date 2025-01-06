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
    Review getById(Integer id);
    Page<Review> getAllPagination(Pageable pageable);
    Review save(ReviewDTO reviewDTO, MultipartFile image);
    void delete(Integer id);
    List<Review> findTop8ByOrderByCreatedAtDesc();
    Review findByAppointmentId(Integer id);
    Review findByProposalId(Integer id);
    List<Review> findByShop(Integer shopId);

    Page<Review> findByCurrentShop(Pageable pageable);
    Page<Review> searchReviews(String searchTerm, Pageable pageable);
    Page<Review> searchReviewsInCurrentShop(String searchTerm, Pageable pageable);
}
