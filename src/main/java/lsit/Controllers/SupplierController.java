
package lsit.Controllers;

import lsit.Models.Supplier;
import lsit.Repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    @PostMapping
    public Supplier addSupplier(@RequestBody Supplier supplier) {
        return supplierRepository.save(supplier);
    }
}
