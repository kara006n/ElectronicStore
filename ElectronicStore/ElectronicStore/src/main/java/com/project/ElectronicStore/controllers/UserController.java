package com.project.ElectronicStore.controllers;

import com.project.ElectronicStore.dtos.ApiResponseMessage;
import com.project.ElectronicStore.dtos.ImageResponse;
import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.dtos.UserDto;
import com.project.ElectronicStore.services.FileService;
import com.project.ElectronicStore.services.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@Api(value="UserController",description = "Rest APIs to perform user related operations")
public class UserController {

      @Value("${user.profile.image.path}")
      private String imageUploadPath;

      @Autowired
      private UserService userService;

      @Autowired
      private FileService fileService;

      private Logger logger = LoggerFactory.getLogger(UserController.class);

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
      public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
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


      //upload user image

      @PostMapping("/image/{userId}")
      public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage")MultipartFile image, @PathVariable String userId) throws IOException {
            String imageName = fileService.uploadFile(image, imageUploadPath);
            UserDto user = userService.getUserById(userId);
            user.setImageName(imageName);
            UserDto userDto = userService.updateUser(user, userId);
            ImageResponse imageResponse = ImageResponse.builder()
                    .imageName(imageName)
                    .success(true)
                    .message("image uploaded successfully")
                    .status(HttpStatus.CREATED).build();
            return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);

      }


      //serve user image
      @GetMapping("/image/{userId}")
      public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

            UserDto user = userService.getUserById(userId);
            logger.info("User Image Name : {}", user.getImageName());
            InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(resource, response.getOutputStream());
            resource.close();

      }


}

