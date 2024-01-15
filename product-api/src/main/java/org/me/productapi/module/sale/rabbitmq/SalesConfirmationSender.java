package org.me.productapi.module.sale.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.me.productapi.module.sale.dto.SalesConfirmationDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SalesConfirmationSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app-config.rabbit.exchange.product}")
    private String productTopicExchange;

    @Value("${app-config.rabbit.routingKey.sales-confirmation}")
    private String salesConfirmationRoutingKey;

    public void sendSalesConfirmationMessage(SalesConfirmationDTO message){

        try {

            log.info("Sending message: {}", new ObjectMapper().writeValueAsString(message));
            rabbitTemplate.convertAndSend(productTopicExchange, salesConfirmationRoutingKey, message);
            log.info("Message was sent successfully!");
        } catch (Exception e){

            log.info("Error while try to send the message: ", e.getMessage());
        }
    }
}
