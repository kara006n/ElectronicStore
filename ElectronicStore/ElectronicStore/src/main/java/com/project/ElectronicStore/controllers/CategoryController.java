package com.project.ElectronicStore.controllers;

import com.project.ElectronicStore.dtos.*;
import com.project.ElectronicStore.services.CategoryService;
import com.project.ElectronicStore.services.FileService;
import com.project.ElectronicStore.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/categories")
public class CategoryController {

      @Value("${category.profile.image.path}")
      private String imageUploadPath;

      private Logger logger = LoggerFactory.getLogger(CategoryController.class);

      @Autowired
      CategoryService categoryService;

      @Autowired
      ProductService productService;

      @Autowired
      FileService fileService;

      //create
      @PreAuthorize("hasRole('ADMIN')")
      @PostMapping
      public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto)
      {
            //call service to save object
            CategoryDto categoryDto1 = categoryService.create(categoryDto);
            return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);
      }
      //update
      @PreAuthorize("hasRole('ADMIN')")
      @PutMapping("/{categoryId}")
      public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable String categoryId){

            CategoryDto updatedCategory = categoryService.update(categoryDto, categoryId);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
      }


      //delete
      @PreAuthorize("hasRole('ADMIN')")
      @DeleteMapping("/{categoryId}")
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
              @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
              @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
              @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
      ){
            PageableResponse<CategoryDto> pageableResponse = categoryService.getAll(pageNumber, pageSize, sortBy, sortDir);
            return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
      }

      //get single
      @GetMapping("/{categoryId}")
      public ResponseEntity<CategoryDto> getSingle(@PathVariable String categoryId){
            CategoryDto categoryDto = categoryService.get(categoryId);
            return ResponseEntity.ok(categoryDto);
      }

      //upload image
      @PostMapping("/image/{categoryId}")
      public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestParam("categoryImage") MultipartFile file, @PathVariable String categoryId) throws IOException {
            String imageName = fileService.uploadFile(file, imageUploadPath);
            CategoryDto category = categoryService.get(categoryId);
            category.setCoverImage(imageName);
            CategoryDto updated = categoryService.update(category, categoryId);
            ImageResponse image_uploaded_successfully = ImageResponse.builder()
                    .message("image uploaded successfully")
                    .status(HttpStatus.OK)
                    .success(true)
                    .imageName(imageName).build();
            return new ResponseEntity<>(image_uploaded_successfully,HttpStatus.CREATED);

      }

      //serve image

      @GetMapping("/image/{categoryId}")
      public void serveCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
            CategoryDto categoryDto = categoryService.get(categoryId);
            logger.info("Category Image Name : {}", categoryDto.getCoverImage());
            InputStream resource = fileService.getResource(imageUploadPath, categoryDto.getCoverImage());
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(resource, response.getOutputStream());
            resource.close();
      }


      @PostMapping("{categoryId}/products")
      public ResponseEntity<ProductDto> createProductWithCategory(@RequestBody ProductDto productDto,
                                                                  @PathVariable String categoryId){

            ProductDto productWithCategory = productService.createWithCategory(productDto, categoryId);
            return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);


      }

      @PutMapping("{categoryId}/products/{productId}")
      public ResponseEntity<ProductDto> updateCategoryOfProduct(@PathVariable String productId,
                                                                  @PathVariable String categoryId){

            ProductDto productWithCategory = productService.updateCategory(productId,categoryId);
            return new ResponseEntity<>(productWithCategory, HttpStatus.OK);
      }

      //get products of specific category
      @GetMapping("{categoryId}/products")
      public ResponseEntity<PageableResponse<ProductDto>> getProductsOfCategory(
              @PathVariable String categoryId,
              @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
              @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
              @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
      ){
            logger.info("Neil kamal" );
            PageableResponse<ProductDto> response = productService.getAllProductOfCategory(categoryId, pageSize, pageNumber, sortBy, sortDir);
            return new ResponseEntity<>(response,HttpStatus.OK);


      }


}
