package com.project.ElectronicStore.services;

import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.dtos.UserDto;

import java.io.IOException;
import java.util.List;

public interface UserService {

      //create
      UserDto createUser(UserDto userDto);

      //update
      UserDto updateUser(UserDto userDto, String userId);

      //delete user
      void deleteUser(String userId) ;

      //get all users
      PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

      //get single users by id
      UserDto getUserById(String userId);

      //get single user by email
      UserDto getUserByEmail(String email);

      //search users
      List<UserDto> searchUser(String keyword);
}
