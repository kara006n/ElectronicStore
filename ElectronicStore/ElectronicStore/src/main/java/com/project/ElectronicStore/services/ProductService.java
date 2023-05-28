package com.project.ElectronicStore.services;

import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.dtos.ProductDto;

import java.util.List;

public interface ProductService {

      //create
      ProductDto create(ProductDto productDto);
      //update
      ProductDto update(ProductDto productDto, String productId);
      //delete
      void delete(String productId);
      //get single
      ProductDto get(String productId);
      //get all
      PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
      //get all :live
      PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);
      //search product
      PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);

      //create product with category
      ProductDto createWithCategory(ProductDto productDto, String categoryId);

      //Update category of product
      ProductDto updateCategory(String productId, String categoryId);

      //search all product under category
      PageableResponse<ProductDto> getAllProductOfCategory(String categoryId, int pageSize, int pageNumber, String sortBy, String sortDir);
      //other methods


}
