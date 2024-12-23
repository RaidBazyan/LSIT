package lsit.Controllers;

import lsit.Models.Order;
import lsit.Models.Customer;
import lsit.Repositories.OrderRepository;
import lsit.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    // Create
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        // Verify customer exists
        Customer customer = customerRepository.get(order.getCustomerId());
        if (customer == null) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Customer not found"));
        }

        // Initialize order status
        order.setStatus(Order.OrderStatus.PENDING);
        orderRepository.add(order);
        return ResponseEntity.ok(order);
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = orderRepository.get(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerOrders(@PathVariable Long customerId) {
        // Verify customer exists
        Customer customer = customerRepository.get(customerId);
        if (customer == null) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("message", "Customer not found"));
        }

        List<Order> orders = orderRepository.getCustomerOrders(customerId);
        if (orders.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("message",
                    "No orders found for customer " + customerId));
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/listall")
    public ResponseEntity<?> listAllOrders() {
        List<Order> orders = orderRepository.list();
        if (orders.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No orders currently stored."));
        }
        return ResponseEntity.ok(orders);
    }

    // Update
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        Order order = orderRepository.get(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.updateStatus(id, status);
        return ResponseEntity.ok(orderRepository.get(id));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        Order existingOrder = orderRepository.get(id);
        if (existingOrder == null) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.remove(id);
        return ResponseEntity.noContent().build();
    }
}