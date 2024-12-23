package com.list_project.lsit.Controllers;

import com.list_project.lsit.Models.WarehouseManager;
import com.list_project.lsit.Repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/warehouse-managers")
public class WarehouseManagerController {

    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseManagerController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    // Create
    @PostMapping("")
    public ResponseEntity<WarehouseManager> createManager(@RequestBody WarehouseManager manager) {
        warehouseRepository.add(manager);
        return ResponseEntity.ok(manager);
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseManager> getManager(@PathVariable Long id) {
        WarehouseManager manager = warehouseRepository.get(id);
        return manager != null ? ResponseEntity.ok(manager) : ResponseEntity.notFound().build();
    }

    @GetMapping("/listall")
    public ResponseEntity<?> listAllManagers() {
        List<WarehouseManager> managers = warehouseRepository.list();
        if (managers.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No warehouse managers currently stored."));
        }
        return ResponseEntity.ok(managers);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseManager> updateManager(@PathVariable Long id, @RequestBody WarehouseManager updatedManager) {
        WarehouseManager existingManager = warehouseRepository.get(id);
        if (existingManager == null) {
            return ResponseEntity.notFound().build();
        }
        updatedManager.setId(id);
        warehouseRepository.add(updatedManager);
        return ResponseEntity.ok(updatedManager);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long id) {
        WarehouseManager existingManager = warehouseRepository.get(id);
        if (existingManager == null) {
            return ResponseEntity.notFound().build();
        }
        warehouseRepository.remove(id);
        return ResponseEntity.noContent().build();
    }
}