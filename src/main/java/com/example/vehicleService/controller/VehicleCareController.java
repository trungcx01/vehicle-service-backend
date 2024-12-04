package com.example.vehicleService.controller;

import com.example.vehicleService.dto.ResponseMessage;
import com.example.vehicleService.dto.VehicleCareDTO;
import com.example.vehicleService.entity.VehicleCare;
import com.example.vehicleService.service.VehicleCareService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/vehicle-cares")
public class VehicleCareController {
    private final VehicleCareService vehicleCareService;

    public VehicleCareController(VehicleCareService vehicleCareService) {
        this.vehicleCareService = vehicleCareService;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return ResponseEntity.ok(vehicleCareService.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable){
        return ResponseEntity.ok(vehicleCareService.getAllPagination(pageable));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> add(@RequestPart VehicleCareDTO vehicleCareDTO, @RequestParam("image")MultipartFile image){
        return ResponseEntity.ok(vehicleCareService.save(vehicleCareDTO, image));
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(@RequestPart VehicleCareDTO vehicleCareDTO, @RequestParam(value = "image", required = false)MultipartFile image){
        return ResponseEntity.ok(vehicleCareService.save(vehicleCareDTO, image));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        vehicleCareService.delete(id);
        return ResponseEntity.ok(new ResponseMessage("Delete Vehicle care successfully!", LocalDateTime.now()));
    }

    @GetMapping("/shop-{shopId}")
    public ResponseEntity<?> getByShop(@PathVariable Long shopId){
        return ResponseEntity.ok(vehicleCareService.getByShop(shopId));
    }

    @GetMapping("/current-shop")
    public ResponseEntity<?> getByShop(){
        return ResponseEntity.ok(vehicleCareService.getByShop());
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByName(@RequestParam(value = "name") String name, @RequestParam(value = "start", required = false) Long start, @RequestParam(value = "end", required = false) Long end){
        return ResponseEntity.ok(vehicleCareService.search(name, start, end));
    }

}
