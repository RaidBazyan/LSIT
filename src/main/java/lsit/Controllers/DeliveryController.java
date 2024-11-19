
package lsit.Controllers;

import lsit.Models.DeliveryDriver;
import lsit.Repositories.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @PostMapping
    public DeliveryDriver addDriver(@RequestBody DeliveryDriver driver) {
        return deliveryRepository.save(driver);
    }
}
