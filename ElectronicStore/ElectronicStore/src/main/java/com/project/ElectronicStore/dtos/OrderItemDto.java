package com.project.ElectronicStore.dtos;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {

      private int orderItemId;
      private int quantity;
      private double totalPrice;
      private ProductDto product;
}
