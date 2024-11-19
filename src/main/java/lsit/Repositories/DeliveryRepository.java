
package lsit.Repositories;

import lsit.Models.DeliveryDriver;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DeliveryRepository {
    private final Map<Long, DeliveryDriver> deliveryDrivers = new HashMap<>();

    public DeliveryDriver save(DeliveryDriver driver) {
        deliveryDrivers.put(driver.getId(), driver);
        return driver;
    }

    public DeliveryDriver findById(Long id) {
        return deliveryDrivers.get(id);
    }
}
