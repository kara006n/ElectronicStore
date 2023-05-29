package com.project.ElectronicStore.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddItemToCartRequest {

      private String productId;
      private int quantity;
}
