package com.project.ElectronicStore.services.impl;

import com.project.ElectronicStore.dtos.CategoryDto;
import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.entities.Category;
import com.project.ElectronicStore.exception.ResourceNotFoundException;
import com.project.ElectronicStore.helper.Helper;
import com.project.ElectronicStore.repositories.CategoryRepository;
import com.project.ElectronicStore.services.CategoryService;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

      @Autowired
      private CategoryRepository categoryRepository;

      @Autowired
      private ModelMapper mapper;

      @Override
      public CategoryDto create(CategoryDto categoryDto) {
            //creating categoryId randomly
            String categoryId = UUID.randomUUID().toString();
            categoryDto.setCategoryId(categoryId);
            Category category = mapper.map(categoryDto, Category.class);
            Category savedCategory = categoryRepository.save(category);
            return mapper.map(savedCategory, CategoryDto.class);
      }

      @Override
      public CategoryDto update(CategoryDto categoryDto, String categoryId) {

            //get category of id
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found with given Id"));

            //update category details
            category.setTitle(categoryDto.getTitle());
            category.setDescription(categoryDto.getDescription());
            category.setCoverImage(categoryDto.getCoverImage());
            Category updatedCategory = categoryRepository.save(category);
            return mapper.map(updatedCategory, CategoryDto.class);
      }

      @Override
      public void delete(String categoryId) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found with given Id"));
            categoryRepository.delete(category);

      }

      @Override
      public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

            Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<Category> page = categoryRepository.findAll(pageable);
            //PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
            return Helper.getPageableResponse(page, CategoryDto.class);
      }

      @Override
      public CategoryDto get(String categoryId) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found with given Id"));
            return mapper.map(category, CategoryDto.class);
      }
}
