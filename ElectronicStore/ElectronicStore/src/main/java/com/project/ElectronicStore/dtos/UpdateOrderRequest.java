package com.project.ElectronicStore.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderRequest {

      private String orderStatus;
      private String paymentStatus;
      private String deliveryDate;
}
