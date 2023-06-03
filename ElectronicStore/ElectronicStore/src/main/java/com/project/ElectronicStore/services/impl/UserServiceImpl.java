package com.project.ElectronicStore.services.impl;

import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.dtos.UserDto;
import com.project.ElectronicStore.entities.User;
import com.project.ElectronicStore.exception.ResourceNotFoundException;
import com.project.ElectronicStore.helper.Helper;
import com.project.ElectronicStore.repositories.UserRepository;
import com.project.ElectronicStore.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl  implements UserService {

      @Autowired
      private PasswordEncoder passwordEncoder;

      @Autowired
      private UserRepository userRepository;

      @Autowired
      private ModelMapper mapper;

      @Value("${user.profile.image.path}")
      private String imagePath;

      Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

      @Override
      public UserDto createUser(UserDto userDto) {

            //generate unique id in string format
            String userId = UUID.randomUUID().toString();
            userDto.setUserId(userId);
            //encoding password before saving
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User user = dtoToEntity(userDto);
            User savedUser = userRepository.save(user);
            return entityToDto(savedUser);
            //return newDto;
      }


      @Override
      public UserDto updateUser(UserDto userDto, String userId) {

            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given Id"));
            user.setName(userDto.getName());
            //user.setEmail(userDto.getEmail());
            user.setGender(userDto.getGender());
            user.setAbout(userDto.getAbout());
            user.setPassword(userDto.getPassword());
            user.setImageName(userDto.getImageName());

            User updatedUser = userRepository.save(user);
            return entityToDto(updatedUser);
            //return updatedDto;
      }

      @Override
      public void deleteUser(String userId)  {

            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given Id"));

            //delete user profile image
            String fullPath = imagePath + user.getImageName();
            try{
                  Path path = Paths.get(fullPath);
                  Files.delete(path);
            }catch (NoSuchFileException ex) {
                  logger.info("user_image_not_found_in_folder");
                  ex.printStackTrace();
            }
            catch (IOException e) {
                  e.printStackTrace();
            }
            //delete user
            userRepository.delete(user);

      }

      @Override
      public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

            Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc")? Sort.Direction.ASC: Sort.Direction.DESC,sortBy);
            Pageable pageable = PageRequest.of(pageNumber,pageSize, sort);
            Page<User> page = userRepository.findAll(pageable);

            PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);
            return response;
      }

      @Override
      public UserDto getUserById(String userId) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with given id"));
            //UserDto userDto = entityToDto(user);
            return entityToDto(user);
      }

      @Override
      public UserDto getUserByEmail(String email) {

            User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("user not found with given email"));
            return entityToDto(user);
      }

      @Override
      public List<UserDto> searchUser(String keyword) {
            List<User> users = userRepository.findByNameContaining(keyword);
            //List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
            return users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
            //return dtoList;
      }

      private UserDto entityToDto(User savedUser) {

//           UserDto userDto =  UserDto.builder()
//                    .userId(savedUser.getUserId())
//                    .name(savedUser.getImageName())
//                    .email(savedUser.getEmail())
//                    .password(savedUser.getPassword())
//                    .gender(savedUser.getGender())
//                    .about(savedUser.getAbout())
//                    .imageName(savedUser.getImageName()).build();

                        return mapper.map(savedUser, UserDto.class);

                    //return userDto;
      }

      private User dtoToEntity(UserDto userDto) {

//            User user = User.builder()
//                    .userId(userDto.getUserId())
//                    .name(userDto.getImageName())
//                    .email(userDto.getEmail())
//                    .password(userDto.getPassword())
//                    .gender(userDto.getGender())
//                    .about(userDto.about)
//                    .imageName(userDto.getImageName()).build();
//
//            return user;

                  return mapper.map(userDto, User.class);
      }
}

