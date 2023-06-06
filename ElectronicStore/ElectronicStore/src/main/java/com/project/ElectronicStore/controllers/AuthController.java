package com.project.ElectronicStore.controllers;

import com.project.ElectronicStore.dtos.UserDto;
import com.project.ElectronicStore.dtos.JwtRequest;
import com.project.ElectronicStore.dtos.JwtResponse;
import com.project.ElectronicStore.exception.BadApiRequestException;
import com.project.ElectronicStore.security.JwtHelper;
import com.project.ElectronicStore.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {

      @Autowired
      private ModelMapper mapper;

      @Autowired
      private UserDetailsService userDetailsService;

      @Autowired
      private AuthenticationManager authenticationManager;

      @Autowired
      private UserService userService;

      @Autowired
      private JwtHelper helper;

      @GetMapping("/current")
      public ResponseEntity<UserDto> getCurrentUser(Principal principal){
            String name = principal.getName();

            return  new ResponseEntity(mapper.map(userDetailsService.loadUserByUsername(name), UserDto.class), HttpStatus.OK);

      }

      @PostMapping("/login")
      public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){

            this.doAuthenticate(request.getEmail(), request.getPassword());
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = helper.generateToken(userDetails);

            UserDto userDto = mapper.map(userDetails, UserDto.class);
            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .user(userDto).build();


            return new ResponseEntity<>(response,HttpStatus.OK);
      }

      private void doAuthenticate(String email, String password) {
            UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(email, password);
            try {
                  authenticationManager.authenticate(authenticate);
            }catch (BadCredentialsException bce){
                  throw new BadApiRequestException("Invalid UserName or Password");
            }
      }


}
