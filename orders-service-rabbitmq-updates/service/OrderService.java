package com.ecommerce.orders.service;

import com.ecommerce.orders.client.UserClient;
import com.ecommerce.orders.dto.OrderDTO;
import com.ecommerce.orders.dto.OrderDetailsDTO;
import com.ecommerce.orders.dto.UserDTO;
import com.ecommerce.orders.entity.Order;
import com.ecommerce.orders.event.OrderCreatedEvent;
import com.ecommerce.orders.publisher.EventPublisher;
import com.ecommerce.orders.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserClient userClient;
    private final EventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, UserClient userClient, EventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
        this.eventPublisher = eventPublisher;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return convertToDTO(order);
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        boolean userExists = checkIfUserExists(orderDTO.getUserId());
        
        if (!userExists) {
            throw new RuntimeException("User with ID " + orderDTO.getUserId() + " does not exist");
        }

        Order order = convertToEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        
        publishOrderCreatedEvent(savedOrder);
        
        return convertToDTO(savedOrder);
    }
    
    private void publishOrderCreatedEvent(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent(
            order.getId(),
            order.getUserId(),
            order.getProductName(),
            order.getQuantity(),
            order.getPrice(),
            order.getPrice() * order.getQuantity(), // totalAmount
            order.getStatus(),
            LocalDateTime.now()
        );
        
        eventPublisher.publishOrderCreatedEvent(event);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "checkIfUserExistsFallback")
    @Retry(name = "userService")
    private boolean checkIfUserExists(Long userId) {
        Map<String, Boolean> userExistsResponse = userClient.userExists(userId);
        return userExistsResponse.get("exists");
    }

    private boolean checkIfUserExistsFallback(Long userId, Exception ex) {
        throw new RuntimeException("Users service is currently unavailable. Cannot validate user. Please try again later.");
    }

    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        existingOrder.setProductName(orderDTO.getProductName());
        existingOrder.setQuantity(orderDTO.getQuantity());
        existingOrder.setPrice(orderDTO.getPrice());
        if (orderDTO.getStatus() != null) {
            existingOrder.setStatus(orderDTO.getStatus());
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return convertToDTO(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "getOrderDetailsFallback")
    @Retry(name = "userService")
    public OrderDetailsDTO getOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        UserDTO user = userClient.getUserById(order.getUserId());

        OrderDetailsDTO details = new OrderDetailsDTO();
        details.setOrderId(order.getId());
        details.setProductName(order.getProductName());
        details.setQuantity(order.getQuantity());
        details.setPrice(order.getPrice());
        details.setStatus(order.getStatus());
        details.setUserId(user.getId());
        details.setUserName(user.getFirstName() + " " + user.getLastName());
        details.setUserEmail(user.getEmail());
        details.setUserAddress(user.getAddress());
        details.setUserPhone(user.getPhoneNumber());

        return details;
    }

    public OrderDetailsDTO getOrderDetailsFallback(Long orderId, Exception ex) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        OrderDetailsDTO details = new OrderDetailsDTO();
        details.setOrderId(order.getId());
        details.setProductName(order.getProductName());
        details.setQuantity(order.getQuantity());
        details.setPrice(order.getPrice());
        details.setStatus(order.getStatus());
        details.setUserId(order.getUserId());
        details.setUserName("Informacije o korisniku trenutno nisu dostupne");
        details.setUserEmail("N/A");
        details.setUserAddress("N/A");
        details.setUserPhone("N/A");

        return details;
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setProductName(order.getProductName());
        dto.setQuantity(order.getQuantity());
        dto.setPrice(order.getPrice());
        dto.setStatus(order.getStatus());
        return dto;
    }

    private Order convertToEntity(OrderDTO dto) {
        Order order = new Order();
        order.setUserId(dto.getUserId());
        order.setProductName(dto.getProductName());
        order.setQuantity(dto.getQuantity());
        order.setPrice(dto.getPrice());
        if (dto.getStatus() != null) {
            order.setStatus(dto.getStatus());
        }
        return order;
    }
}
