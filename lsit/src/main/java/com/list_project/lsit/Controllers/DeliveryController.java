package com.list_project.lsit.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.list_project.lsit.Models.DeliveryDriver;
import com.list_project.lsit.Repositories.DeliveryRepository;

import java.util.List;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;

    public DeliveryController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @PostMapping
    public ResponseEntity<DeliveryDriver> addDriver(@RequestBody DeliveryDriver driver) {
        deliveryRepository.add(driver);
        return ResponseEntity.ok(driver);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDriver> getDriver(@PathVariable Long id) {
        DeliveryDriver driver = deliveryRepository.get(id);
        if (driver == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(driver);
    }

    @GetMapping
    public ResponseEntity<List<DeliveryDriver>> listDrivers() {
        return ResponseEntity.ok(deliveryRepository.list());
    }
}
