package com.list_project.lsit.Repositories;

import java.util.*;

import org.springframework.stereotype.Repository;

import com.list_project.lsit.Models.WarehouseManager;

@Repository
public class WarehouseRepository {
    private static final Map<Long, WarehouseManager> warehouseManagers = new HashMap<>();
    private static long currentId = 1;

    public void add(WarehouseManager manager) {
        manager.setId(currentId++);
        warehouseManagers.put(manager.getId(), manager);
    }

    public WarehouseManager get(Long id) {
        return warehouseManagers.get(id);
    }

    public void remove(Long id) {
        warehouseManagers.remove(id);
    }

    public List<WarehouseManager> list() {
        return new ArrayList<>(warehouseManagers.values());
    }
}
