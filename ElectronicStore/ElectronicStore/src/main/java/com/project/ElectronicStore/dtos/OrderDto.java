package com.project.ElectronicStore.dtos;

import com.project.ElectronicStore.entities.OrderItem;
import com.project.ElectronicStore.entities.User;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

      private String orderId;
      private String orderStatus = "PENDING";
      private String paymentStatus = "NOTPAID";
      private double orderAmount;
      private String billingAddress;
      private String billingPhone;
      private String billingName;
      private Date orderedDate = new Date();
      private String deliveryDate;
      //private UserDto user;
      private List<OrderItemDto> orderItem = new ArrayList<>();

}
