package com.ecommerce.notification.consumer;

import com.ecommerce.notification.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderNotificationListener.class);

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

        sendEmailNotification(event);
        sendSMSNotification(event);
        logNotification(event);

        logger.info("✅ Sve notifikacije poslate uspešno!");
    }
cija slanja email notifikacije
     */
    private void sendEmailNotification(OrderCreatedEvent event) {
        logger.info("📧 Email poslat korisniku {} o porudžbini #{}", 
                   event.getUserId(), event.getOrderId());
        
        // Ovde bi išla logika za slanje emaila
        // Na primer: JavaMailSender, SendGrid, AWS SES, itd.
        // emailService.sendOrderConfirmation(event);
    }

    private void sendSMSNotification(OrderCreatedEvent event) {
        logger.info("📱 SMS poslat korisniku {} - porudžbina je kreirana", 
                   event.getUserId());
        
        // Ovde bi išla logika za slanje SMS-a
        // Na primer: Twilio, AWS SNS, itd.
        // smsService.sendOrderNotification(event);
    }

    private void logNotification(OrderCreatedEvent event) {
        logger.info("📝 Notifikacija zabeležena u sistemu - Order #{}", 
                   event.getOrderId());
        
        // Ovde bi išla logika za čuvanje notifikacije u bazi
        // notificationRepository.save(notification);
    }
}
