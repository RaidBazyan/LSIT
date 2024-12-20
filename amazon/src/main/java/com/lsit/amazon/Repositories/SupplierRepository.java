package lsit.Repositories;

import java.util.*;

import org.springframework.stereotype.Repository;

import lsit.Models.Supplier;

@Repository
public class SupplierRepository {
    private static final Map<Long, Supplier> suppliers = new HashMap<>();
    private static long currentId = 1;

    public void add(Supplier supplier) {
        supplier.setId(currentId++);
        suppliers.put(supplier.getId(), supplier);
    }

    public Supplier get(Long id) {
        return suppliers.get(id);
    }

    public void remove(Long id) {
        suppliers.remove(id);
    }

    public void update(Supplier supplier) {
        Supplier existing = suppliers.get(supplier.getId());
        if (existing != null) {
            existing.setName(supplier.getName());
            existing.setContact(supplier.getContact());
        }
    }

    public List<Supplier> list() {
        return new ArrayList<>(suppliers.values());
    }
}
