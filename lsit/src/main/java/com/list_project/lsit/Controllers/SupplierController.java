package com.list_project.lsit.Controllers;

import com.list_project.lsit.Models.Supplier;
import com.list_project.lsit.Repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // Create
    @PostMapping("")
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
        supplierRepository.add(supplier);
        return ResponseEntity.ok(supplier);
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplier(@PathVariable Long id) {
        Supplier supplier = supplierRepository.get(id);
        return supplier != null ? ResponseEntity.ok(supplier) : ResponseEntity.notFound().build();
    }

    @GetMapping("/listall")
    public ResponseEntity<?> listAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.list();
        if (suppliers.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No suppliers currently stored."));
        }
        return ResponseEntity.ok(suppliers);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier updatedSupplier) {
        Supplier existingSupplier = supplierRepository.get(id);
        if (existingSupplier == null) {
            return ResponseEntity.notFound().build();
        }
        updatedSupplier.setId(id);
        supplierRepository.update(updatedSupplier);
        return ResponseEntity.ok(updatedSupplier);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        Supplier existingSupplier = supplierRepository.get(id);
        if (existingSupplier == null) {
            return ResponseEntity.notFound().build();
        }
        supplierRepository.remove(id);
        return ResponseEntity.noContent().build();
    }
}