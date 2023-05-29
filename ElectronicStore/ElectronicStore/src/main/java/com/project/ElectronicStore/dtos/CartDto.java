package com.project.ElectronicStore.dtos;

import com.project.ElectronicStore.entities.CartItem;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

      private String cartId;
      private Date createdDate;
      private UserDto user;
      //mapping with cart item entity
      private List<CartItemDto> items = new ArrayList<>();
}
