package com.project.ElectronicStore.entities;

import lombok.*;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class User {

      @Id
      //@GeneratedValue(strategy = GenerationType.IDENTITY )
      @Column(name="user_id")
      private String userId;

      @Column(name = "user_name")
      private String name;

      @Column(name = "user_email", unique = true)
      private String email;

      @Column(name = "user_password", length = 10)
      private String password;

      private String gender;

      @Column(length = 1000)
      public String about;

      @Column(name = "user_image_name")
      private String imageName;

}


