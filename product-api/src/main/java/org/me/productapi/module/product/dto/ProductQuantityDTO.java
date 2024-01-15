package org.me.productapi.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantityDTO {

    private Integer productId;
    private Integer quantity;
}
