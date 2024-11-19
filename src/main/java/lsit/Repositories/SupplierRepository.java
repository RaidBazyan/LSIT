
package lsit.Repositories;

import lsit.Models.Supplier;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SupplierRepository {
    private final Map<Long, Supplier> suppliers = new HashMap<>();

    public Supplier save(Supplier supplier) {
        suppliers.put(supplier.getId(), supplier);
        return supplier;
    }

    public Supplier findById(Long id) {
        return suppliers.get(id);
    }
}
