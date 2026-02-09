package com.ecommerce.orders.publisher;

import com.ecommerce.orders.config.RabbitMQConfig;
import com.ecommerce.orders.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrderCreatedEvent(OrderCreatedEvent event) {
        logger.info("========================================");
        logger.info("📤 Slanje OrderCreatedEvent na RabbitMQ:");
        logger.info("   Order ID: {}", event.getOrderId());
        logger.info("   Exchange: {}", RabbitMQConfig.EXCHANGE);
        logger.info("   Routing Key: {}", RabbitMQConfig.ROUTING_KEY);
        logger.info("========================================");

        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                event
            );
            logger.info("✅ Event uspešno poslat na RabbitMQ!");
        } catch (Exception e) {
            logger.error("❌ Greška pri slanju eventa na RabbitMQ: {}", e.getMessage(), e);
        }
    }
}
