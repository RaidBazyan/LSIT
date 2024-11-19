
package lsit.Controllers;

import lsit.Models.WarehouseManager;
import lsit.Repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @PostMapping
    public WarehouseManager addManager(@RequestBody WarehouseManager manager) {
        return warehouseRepository.save(manager);
    }
}
