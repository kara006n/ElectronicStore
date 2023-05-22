package com.project.ElectronicStore.helper;

import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.dtos.UserDto;
import com.project.ElectronicStore.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

      //here we can assume that U is of type Entity and V is of type DTO and they can be of product user or etc.
      //in modelMapper map method we have to pass class literal like User.class etc but here we want to pass generic value
      //so that it can be used for every modules that's why we need value of V along with U. how will this class get value of V
      //it needs to be passed while method call so recieve its value in method parameters. xyz.class actually gives class literal
      //i.e. it is object of class and it is stored like Class<?> clazz = MyClass.class; so right side value we need to pass through
      //method call and left side declaration we should write in method parameter to receive that value.
      //MyClass.class creates class object which contains all information of class it is concept of reflection then we can use that
      //information to access class at runtime.
      public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type)
      {
            List<U> entity = page.getContent();
            //List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
            List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object, type)).collect(Collectors.toList());

            PageableResponse<V> response = new PageableResponse<>();
            response.setContent(dtoList);
            response.setPageNumber(page.getNumber());
            response.setPageSize(page.getSize());
            response.setTotalElements(page.getTotalElements());
            response.setTotalPages(page.getTotalPages());
            response.setLastPage(page.isLast());
            return response;
      }
}
