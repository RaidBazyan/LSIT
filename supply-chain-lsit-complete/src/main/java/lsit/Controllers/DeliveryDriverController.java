package lsit.Controllers;

import lsit.Models.DeliveryDriver;
import lsit.Repositories.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/delivery-drivers")
public class DeliveryDriverController {

    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryDriverController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    // Create
    @PostMapping("")
    public ResponseEntity<DeliveryDriver> createDriver(@RequestBody DeliveryDriver driver) {
        deliveryRepository.add(driver);
        return ResponseEntity.ok(driver);
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDriver> getDriver(@PathVariable Long id) {
        DeliveryDriver driver = deliveryRepository.get(id);
        return driver != null ? ResponseEntity.ok(driver) : ResponseEntity.notFound().build();
    }

    @GetMapping("/listall")
    public ResponseEntity<?> listAllDrivers() {
        List<DeliveryDriver> drivers = deliveryRepository.list();
        if (drivers.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No delivery drivers currently stored."));
        }
        return ResponseEntity.ok(drivers);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDriver> updateDriver(@PathVariable Long id, @RequestBody DeliveryDriver updatedDriver) {
        DeliveryDriver existingDriver = deliveryRepository.get(id);
        if (existingDriver == null) {
            return ResponseEntity.notFound().build();
        }
        updatedDriver.setId(id);
        deliveryRepository.add(updatedDriver);
        return ResponseEntity.ok(updatedDriver);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(@PathVariable Long id) {
        DeliveryDriver existingDriver = deliveryRepository.get(id);
        if (existingDriver == null) {
            return ResponseEntity.notFound().build();
        }
        deliveryRepository.remove(id);
        return ResponseEntity.noContent().build();
    }
}