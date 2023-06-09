package com.project.ElectronicStore.dtos;

import com.project.ElectronicStore.entities.Category;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

      private String productId;
      private String title;
      private String description;
      private double price;
      private double discountedPrice;
      private int quantity;
      private Date addedDate;
      private boolean live;
      private boolean stock;
      private String productImage;
      private CategoryDto category;
}
