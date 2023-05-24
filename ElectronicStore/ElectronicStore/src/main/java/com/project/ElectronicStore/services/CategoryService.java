package com.project.ElectronicStore.services;

import com.project.ElectronicStore.dtos.CategoryDto;
import com.project.ElectronicStore.dtos.PageableResponse;

public interface CategoryService {

      //create
      CategoryDto create(CategoryDto categoryDto);

      //update
      CategoryDto update(CategoryDto categoryDto, String categoryId);

      //delete
      void delete(String categoryId);

      //getAll
      PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);

      //getSingleCategory
      CategoryDto get(String categoryId);


      //search
}
