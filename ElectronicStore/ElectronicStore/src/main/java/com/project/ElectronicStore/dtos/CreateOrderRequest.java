package com.project.ElectronicStore.dtos;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {

      @NotBlank(message = "cart id is required")
      private String userId;
      private String orderStatus = "PENDING";
      private String paymentStatus = "NOTPAID";
      @NotBlank(message = "billingAddress is required !!")
      private String billingAddress;
      @NotBlank(message = "billingPhone is required !!")
      @Pattern(regexp = "^[789]\\d{9}$")
      private String billingPhone;
      @NotBlank(message = "billingName is required !!")
      private String billingName;

}
