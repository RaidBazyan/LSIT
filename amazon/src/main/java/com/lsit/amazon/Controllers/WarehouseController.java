package lsit.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lsit.Models.WarehouseManager;
import lsit.Repositories.WarehouseRepository;

import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    private final WarehouseRepository warehouseRepository;

    public WarehouseController(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @PostMapping
    public ResponseEntity<WarehouseManager> addManager(@RequestBody WarehouseManager manager) {
        warehouseRepository.add(manager);
        return ResponseEntity.ok(manager);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseManager> getManager(@PathVariable Long id) {
        WarehouseManager manager = warehouseRepository.get(id);
        if (manager == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(manager);
    }

    @GetMapping
    public ResponseEntity<List<WarehouseManager>> listManagers() {
        return ResponseEntity.ok(warehouseRepository.list());
    }
}
