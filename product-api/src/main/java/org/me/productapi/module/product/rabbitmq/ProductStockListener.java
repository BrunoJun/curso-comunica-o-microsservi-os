package org.me.productapi.module.product.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.me.productapi.module.product.dto.ProductStockDTO;
import org.me.productapi.module.product.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductStockListener {

    @Autowired
    private ProductService service;

    @RabbitListener(queues = "${app-config.rabbit.queue.product-stock}")
    public void receiveProductStockMessage(ProductStockDTO productStockDTO) throws JsonProcessingException {
        log.info(("Receiving message with data: {} and TransactionId {}"),
                new ObjectMapper().writeValueAsString(productStockDTO),
                productStockDTO.getTransactionid());
        service.updateProductStock(productStockDTO);
    }
}
