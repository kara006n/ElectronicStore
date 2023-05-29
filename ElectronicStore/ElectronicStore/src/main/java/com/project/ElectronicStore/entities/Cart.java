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
@Table(name="cart")
public class Cart {

      @Id
      private String cartId;

      private Date createdDate;
      @OneToOne
      private User user;

      //mapping with cart item entity
      @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
      private List<CartItem>items = new ArrayList<>();
}
