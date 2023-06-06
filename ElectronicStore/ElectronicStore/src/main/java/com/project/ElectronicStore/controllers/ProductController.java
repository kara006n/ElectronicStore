package com.project.ElectronicStore.controllers;

import com.project.ElectronicStore.dtos.ApiResponseMessage;
import com.project.ElectronicStore.dtos.ImageResponse;
import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.dtos.ProductDto;
import com.project.ElectronicStore.services.FileService;
import com.project.ElectronicStore.services.ProductService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/products")
@Api(value="ProductController",description = "Rest APIs to perform product related operations")
public class ProductController {

      @Value("${product.image.path}")
      private String imageUploadPath;

      private Logger logger = LoggerFactory.getLogger(ProductController.class);
      
      @Autowired
      FileService fileService;

      @Autowired
      private ProductService productService;

      //create
      @PreAuthorize("hasRole('ADMIN')")
      @PostMapping
      public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
            ProductDto createdProduct = productService.create(productDto);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
      }

      //update
      @PreAuthorize("hasRole('ADMIN')")
      @PutMapping("/{productId}")
      public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable String productId) {
            ProductDto createdProduct = productService.update(productDto, productId);
            return new ResponseEntity<>(createdProduct, HttpStatus.OK);
      }

      //delete
      @PreAuthorize("hasRole('ADMIN')")
      @DeleteMapping("/{productId}")
      public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId){
            productService.delete(productId);
            ApiResponseMessage response = ApiResponseMessage.builder()
                    .message("Product is deleted successfully!!")
                    .status(HttpStatus.OK)
                    .success(true).build();
            return new ResponseEntity<>(response,HttpStatus.OK);
      }

      //get single
      @GetMapping("/{productId}")
      public ResponseEntity<ProductDto> getProduct(@PathVariable String productId) {
            ProductDto productDto = productService.get(productId);
            return new ResponseEntity<>(productDto, HttpStatus.OK);
      }
      //get all
      @GetMapping
      public ResponseEntity<PageableResponse<ProductDto>> getAll(
              @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
              @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
              @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
      ){
            PageableResponse<ProductDto> pageableResponse = productService.getAll(pageNumber, pageSize, sortBy, sortDir);
            return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
      }
      //get all live
      @GetMapping("/live")
      public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
              @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
              @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
              @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
      ){
            PageableResponse<ProductDto> pageableResponse = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
            return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
      }

      //search all
      @GetMapping("/search/{query}")
      public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
              @PathVariable String query,
              @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
              @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
              @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
      ){
            PageableResponse<ProductDto> pageableResponse = productService.searchByTitle(query,pageNumber, pageSize, sortBy, sortDir);
            return new ResponseEntity<>(pageableResponse,HttpStatus.OK);
      }

      //upload  image
      @PreAuthorize("hasRole('ADMIN')")
      @PostMapping("/image/{productId}")
      public ResponseEntity<ImageResponse> uploadProductImage(
              @PathVariable String productId,
              @RequestParam("productImage")MultipartFile image
              ) throws IOException {
            String imageName = fileService.uploadFile(image, imageUploadPath);
            ProductDto productDto = productService.get(productId);
            //logger.info("Neel Kamal : {}", imageName);
            productDto.setProductImage(imageName);
            ProductDto updated = productService.update(productDto, productId);
            ImageResponse image_uploaded_successfully = ImageResponse.builder()
                    .imageName(imageName)
                    .success(true)
                    .status(HttpStatus.CREATED)
                    .message("image uploaded successfully").build();
            return new ResponseEntity<>(image_uploaded_successfully, HttpStatus.CREATED);
      }
      //serve image
      @GetMapping("/image/{productId}")
      public void serveImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
            ProductDto productDto = productService.get(productId);
            //logger.info("Product Image Name : {}", productDto.getProductImage());
            InputStream resource = fileService.getResource(imageUploadPath, productDto.getProductImage());
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            StreamUtils.copy(resource, response.getOutputStream());
            resource.close();
      }

}

