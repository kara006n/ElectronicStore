package com.project.ElectronicStore.dtos;

import com.project.ElectronicStore.dtos.UserDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtResponse {

      private String jwtToken;
      private UserDto user;
}
