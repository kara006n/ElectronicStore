package com.project.ElectronicStore.entities;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="order_items")
public class OrderItem {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private int orderItemId;

      private int quantity;
      private double totalPrice;
      @OneToOne
      @JoinColumn(name="productId")
      private Product product;
      @ManyToOne
      @JoinColumn(name="orderId")
      private Order order;

}
