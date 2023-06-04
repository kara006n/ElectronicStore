package com.project.ElectronicStore.dtos;

import com.project.ElectronicStore.entities.Role;
import com.project.ElectronicStore.validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {


      private String userId;

      @Size(min = 3, max = 20, message = "Invalid Name !!")
      private String name;

//^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\.)+[a-z]{2,5}$
      //
      @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9._%+-]*@[a-zA-Z0-9]+([.]([a-zA-Z]{2,5}))+$",message = "invalid user email")
      @NotBlank(message = "email is required")
      private String email;

      @NotBlank(message = "Password is required !!")
      private String password;

      @Size(min = 4, max = 6, message = "Invalid gender !!")
      private String gender;

      @NotBlank(message = "Field cannot be left blank")
      public String about;

      //pattern
      //custom validator

      @ImageNameValid
      private String imageName;

      private Set<RoleDto> roles = new HashSet<>();
}
