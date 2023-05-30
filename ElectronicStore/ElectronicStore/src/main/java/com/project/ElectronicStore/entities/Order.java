package com.project.ElectronicStore.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="orders")
public class Order {

      @Id
      private String orderId;
      //PENDING, DISPATCHED,DELIVERED
      private String orderStatus;
      //(false)NOT-PAID, (true)PAID
      //PAID, NOT-PAID
      private String paymentStatus;
      private double orderAmount;
      @Column(length=1000)
      private String billingAddress;
      private String billingPhone;
      private String billingName;
      private Date orderedDate;
      private String deliveryDate;
      @ManyToOne(fetch = FetchType.EAGER)
      @JoinColumn(name="userId")
      private User user;
      @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
      private List<OrderItem> orderItem = new ArrayList<>();


}

