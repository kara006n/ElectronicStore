package com.project.ElectronicStore.controllers;

import com.project.ElectronicStore.dtos.ApiResponseMessage;
import com.project.ElectronicStore.dtos.UserDto;
import com.project.ElectronicStore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

      @Autowired
      private UserService userService;

      //create
      @PostMapping
      public ResponseEntity<UserDto> createUser( @Valid @RequestBody UserDto userDto)
      {
            UserDto userDto1 = userService.createUser(userDto);
            return new ResponseEntity<UserDto>(userDto1, HttpStatus.CREATED);
      }

      //update

      @PutMapping("/{userId}")
      public ResponseEntity<UserDto> updateUser(
              @PathVariable("userId") String userId,
              @Valid @RequestBody UserDto userDto
      ){
            UserDto updatedUserDto = userService.updateUser(userDto, userId);
            return new ResponseEntity<UserDto>(updatedUserDto, HttpStatus.OK);
      }

      //delete
      @DeleteMapping("/{userId}")
      public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){
            userService.deleteUser(userId);
            ApiResponseMessage message = ApiResponseMessage.builder()
                    .message("user is deleted successfully !!")
                    .success(true)
                    .status(HttpStatus.OK).build();
            return new ResponseEntity<>(message, HttpStatus.OK);
      }

      //get all
      @GetMapping
      public ResponseEntity<List<UserDto>> getAllUsers(
              @RequestParam(value="pageNumber", defaultValue = "0", required = false) int pageNumber,
              @RequestParam(value="pageSize", defaultValue = "10", required = false) int pageSize,
              @RequestParam(value="sortBy", defaultValue = "name", required = false) String sortBy,
              @RequestParam(value="sortDir", defaultValue = "asc", required = false) String sortDir
      ){
            return new ResponseEntity<>(userService.getAllUser(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
      }

      //get single
      @GetMapping("/{userId}")
      public ResponseEntity<UserDto> getUser(@PathVariable String userId)
      {
            UserDto userDto = userService.getUserById(userId);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
      }

      //get by email
      @GetMapping("/email/{email}")
      public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
      {
            UserDto userDto = userService.getUserByEmail(email);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
      }

      //search user
      @GetMapping("/search/{keywords}")
      public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords)
      {
            return new ResponseEntity<>(userService.searchUser(keywords), HttpStatus.OK);
      }

}

