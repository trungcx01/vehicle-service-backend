package com.example.vehicleService.controller;

import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.dto.ReviewDTO;
import com.example.vehicleService.entity.Review;
import com.example.vehicleService.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable){
        return ResponseEntity.ok(reviewService.getAllPagination(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestPart ReviewDTO reviewDTO, @RequestParam(value = "image", required = false)MultipartFile image){
        return ResponseEntity.ok(reviewService.save(reviewDTO, image));
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestPart ReviewDTO reviewDTO, @RequestParam(value = "image", required = false)MultipartFile image){
        return ResponseEntity.ok(reviewService.save(reviewDTO, image));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        reviewService.delete(id);
        return ResponseEntity.ok(new ResponseMessage("Delete Review successfully!", LocalDateTime.now()));
    }

    @GetMapping("/top8-newest")
    public ResponseEntity<?> top8Newest(){
        return ResponseEntity.ok(reviewService.findTop8ByOrderByCreatedAtDesc());
    }

    @GetMapping("/get-by-appointment/{id}")
    public ResponseEntity<?> getByAppointment(@PathVariable("id") Integer id){
        return ResponseEntity.ok(reviewService.findByAppointmentId(id));
    }

    @GetMapping("/get-by-proposal/{id}")
    public ResponseEntity<?> getByProposal(@PathVariable("id") Integer id){
        return ResponseEntity.ok(reviewService.findByProposalId(id));
    }

    @GetMapping("/get-by-shop/{id}")
    public ResponseEntity<?> getByShop(@PathVariable("id") Integer id){
        return ResponseEntity.ok(reviewService.findByShop(id));
    }

    @GetMapping("/get-by-current-shop")
    public ResponseEntity<?> getByCurrentShop(Pageable pageable){
        return ResponseEntity.ok(reviewService.findByCurrentShop(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Review>> searchReviews(
            @RequestParam(value = "searchTerm") String searchTerm,
            Pageable pageable) {

        Page<Review> reviewPage = reviewService.searchReviews(searchTerm, pageable);
        return ResponseEntity.ok(reviewPage);
    }

    @GetMapping("/search-in-shop")
    public ResponseEntity<Page<Review>> searchReviewsInShop(
            @RequestParam(value = "searchTerm") String searchTerm,
            Pageable pageable) {

        Page<Review> reviewPage = reviewService.searchReviewsInCurrentShop(searchTerm, pageable);
        return ResponseEntity.ok(reviewPage);
    }
}
