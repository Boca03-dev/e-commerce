package com.ecommerce.notification.consumer;

import com.ecommerce.notification.dto.WebSocketNotificationDTO;
import com.ecommerce.notification.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ Consumer - sluša OrderCreatedEvent poruke
 * Šalje real-time notifikacije preko WebSocket-a
 */
@Component
public class OrderNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderNotificationListener.class);
    
    private final SimpMessagingTemplate messagingTemplate;

    public OrderNotificationListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Sluša poruke sa queue-a "order.notifications.queue"
     * Kada stigne OrderCreatedEvent, šalje notifikaciju
     */
    @RabbitListener(queues = "order.notifications.queue")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        logger.info("========================================");
        logger.info("📨 Primljen OrderCreatedEvent:");
        logger.info("   Order ID: {}", event.getOrderId());
        logger.info("   User ID: {}", event.getUserId());
        logger.info("   Product: {}", event.getProductName());
        logger.info("   Quantity: {}", event.getQuantity());
        logger.info("   Total: ${}", event.getTotalAmount());
        logger.info("   Status: {}", event.getStatus());
        logger.info("========================================");

        // Simulacija slanja notifikacija
        sendEmailNotification(event);
        sendSMSNotification(event);
        logNotification(event);
        
        // 🚀 NOVO: Šalji real-time WebSocket notifikaciju!
        sendWebSocketNotification(event);

        logger.info("✅ Sve notifikacije poslate uspešno!");
    }

    /**
     * Simulacija slanja email notifikacije
     */
    private void sendEmailNotification(OrderCreatedEvent event) {
        logger.info("📧 Email poslat korisniku {} o porudžbini #{}", 
                   event.getUserId(), event.getOrderId());
    }

    /**
     * Simulacija slanja SMS notifikacije
     */
    private void sendSMSNotification(OrderCreatedEvent event) {
        logger.info("📱 SMS poslat korisniku {} - porudžbina je kreirana", 
                   event.getUserId());
    }

    /**
     * Loguje notifikaciju u sistem
     */
    private void logNotification(OrderCreatedEvent event) {
        logger.info("📝 Notifikacija zabeležena u sistemu - Order #{}", 
                   event.getOrderId());
    }
    
    /**
     * 🚀 NOVO: Šalje real-time WebSocket notifikaciju browser-u
     */
    private void sendWebSocketNotification(OrderCreatedEvent event) {
        logger.info("📡 Slanje WebSocket notifikacije...");
        
        // Kreiraj notifikaciju za browser
        WebSocketNotificationDTO notification = new WebSocketNotificationDTO();
        notification.setType("ORDER_CREATED");
        notification.setTitle("Nova Porudžbina!");
        notification.setMessage(String.format(
            "Porudžbina #%d je kreirana - %s (x%d) - $%.2f",
            event.getOrderId(),
            event.getProductName(),
            event.getQuantity(),
            event.getTotalAmount()
        ));
        notification.setOrderId(event.getOrderId());
        notification.setUserId(event.getUserId());
        notification.setProductName(event.getProductName());
        notification.setTotalAmount(event.getTotalAmount());
        
        // Pošalji na WebSocket topic
        // Svi povezani klijenti će primiti ovu poruku
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        
        logger.info("✅ WebSocket notifikacija poslata na /topic/notifications");
    }
}
