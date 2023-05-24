package com.project.ElectronicStore.controllers;

import com.project.ElectronicStore.dtos.ApiResponseMessage;
import com.project.ElectronicStore.dtos.CategoryDto;
import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/categories")
public class CategoryController {

      @Autowired
      CategoryService categoryService;

      //create
      @PostMapping
      public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto)
      {
            //call service to save object
            CategoryDto categoryDto1 = categoryService.create(categoryDto);
            return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
      }
      //update
      @PutMapping("/{categoryId")
      public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto, @PathVariable String categoryId){

            CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
      }


      //delete
      @DeleteMapping("/{categoryId")
      public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
            categoryService.delete(categoryId);
            ApiResponseMessage response = ApiResponseMessage.builder()
                    .message("Category is deleted successfully")
                    .status(HttpStatus.OK)
                    .success(true).build();

            return new ResponseEntity<>(response, HttpStatus.OK);
      }

      //get all
      @GetMapping
      public ResponseEntity<PageableResponse<CategoryDto>> getAll(
              @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNUmber,
              @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize,
              @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
              @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
      ){
            PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNUmber, pageSize, sortBy, sortDir);
            return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
      }

      //get single
      @GetMapping("/{categoryId}")
      public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId){
            CategoryDto categoryDto = categoryService.get(categoryId);
            return ResponseEntity.ok(categoryDto);
      }


}
