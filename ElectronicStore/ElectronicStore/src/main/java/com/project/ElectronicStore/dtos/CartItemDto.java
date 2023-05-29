package com.project.ElectronicStore.dtos;


import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

      private int cartItemId;
      private ProductDto product;
      private int quantity;
      private double totalPrice;
}
