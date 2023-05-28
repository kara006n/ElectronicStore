package com.project.ElectronicStore.services.impl;

import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.dtos.ProductDto;
import com.project.ElectronicStore.entities.Category;
import com.project.ElectronicStore.entities.Product;
import com.project.ElectronicStore.exception.ResourceNotFoundException;
import com.project.ElectronicStore.helper.Helper;
import com.project.ElectronicStore.repositories.CategoryRepository;
import com.project.ElectronicStore.repositories.ProductRepository;
import com.project.ElectronicStore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

      @Autowired
      private CategoryRepository categoryRepository;

      @Value("${product.image.path}")
      private String imageUploadPath;

      Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

      @Autowired
      private ProductRepository productRepository;

      @Autowired
      private ModelMapper mapper;

      @Override
      public ProductDto create(ProductDto productDto) {
            Product product = mapper.map(productDto, Product.class);
            String productId = UUID.randomUUID().toString();
            product.setProductId(productId);
            product.setAddedDate(new Date());
            Product save = productRepository.save(product);
            return mapper.map(save, ProductDto.class);
      }

      @Override
      public ProductDto update(ProductDto productDto, String productId) {

            Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given Id"));
            product.setTitle(productDto.getTitle());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setDiscountedPrice(productDto.getDiscountedPrice());
            product.setQuantity(productDto.getQuantity());
            product.setLive(productDto.isLive());
            product.setStock(productDto.isStock());
            product.setProductImage(productDto.getProductImage());
            Product updatedProduct = productRepository.save(product);
            return mapper.map(product, ProductDto.class);
      }

      @Override
      public void delete(String productId) {
            Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given Id"));
            String fullPath = imageUploadPath + product.getProductImage();
            try{
                  Path path = Path.of(fullPath);
                  Files.delete(path);
            }catch(NoSuchFileException ex){
                  logger.info("product image not found");
                  ex.printStackTrace();
            }catch(IOException e){
                  e.printStackTrace();
            }

            productRepository.delete(product);

      }

      @Override
      public ProductDto get(String productId) {
            Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given Id"));
            return mapper.map(product,ProductDto.class);
      }

      @Override
      public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
            Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<Product> products = productRepository.findAll(pageable);
            return Helper.getPageableResponse(products, ProductDto.class);
      }

      @Override
      public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
            Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<Product> products = productRepository.findByLiveTrue(pageable);
            return Helper.getPageableResponse(products, ProductDto.class);
      }

      @Override
      public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
            Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<Product> products = productRepository.findByTitleContaining(subTitle, pageable);
            return Helper.getPageableResponse(products, ProductDto.class);
      }

      @Override
      public ProductDto createWithCategory(ProductDto productDto, String categoryId) {

            //fetch category
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found with given Id"));
            Product product = mapper.map(productDto, Product.class);
            String productId = UUID.randomUUID().toString();
            product.setProductId(productId);
            product.setCategory(category);
            product.setAddedDate(new Date());
            Product save = productRepository.save(product);
            return mapper.map(save, ProductDto.class);
      }

      @Override
      public ProductDto updateCategory(String productId, String categoryId) {
            Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with given Id"));
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found with given Id"));
            product.setCategory(category);
            Product savedProduct = productRepository.save(product);
            return mapper.map(savedProduct, ProductDto.class);
      }

      @Override
      public PageableResponse<ProductDto> getAllProductOfCategory(String categoryId, int pageSize, int pageNumber, String sortBy, String sortDir) {
            Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found with given Id"));
            Page<Product> page = productRepository.findByCategory(category, pageable);
            return Helper.getPageableResponse(page, ProductDto.class);
      }


}
