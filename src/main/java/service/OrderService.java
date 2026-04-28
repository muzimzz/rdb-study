package service;

import domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void save(Order order) {
        orderRepository.save(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public void update(Order order) {
        orderRepository.update(order);
    }

    public void deleteById(Long id) {
        if (findById(id) == null)
            throw new IllegalArgumentException("존재하지 않는 주문");
        orderRepository.deleteById(id);
    }
}
