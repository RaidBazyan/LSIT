
package lsit.Repositories;

import lsit.Models.WarehouseManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class WarehouseRepository {
    private final Map<Long, WarehouseManager> warehouseManagers = new HashMap<>();

    public WarehouseManager save(WarehouseManager manager) {
        warehouseManagers.put(manager.getId(), manager);
        return manager;
    }

    public WarehouseManager findById(Long id) {
        return warehouseManagers.get(id);
    }
}
