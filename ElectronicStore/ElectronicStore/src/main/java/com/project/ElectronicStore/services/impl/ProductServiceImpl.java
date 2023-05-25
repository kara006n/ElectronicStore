package com.project.ElectronicStore.services.impl;

import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.dtos.ProductDto;
import com.project.ElectronicStore.entities.Product;
import com.project.ElectronicStore.helper.Helper;
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

      @Value("${product.image.path}")
      private String imageUploadPath;

      Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

      @Autowired
      private ProductRepository productRepository;

      @Autowired
      private ModelMapper mapper;

      @Override
      public ProductDto create(ProductDto productDto) {
            String productId = UUID.randomUUID().toString();
            productDto.setProductId(productId);
            productDto.setAddedDate(new Date());
            Product product = mapper.map(productDto, Product.class);
            Product save = productRepository.save(product);
            return mapper.map(save, ProductDto.class);
      }

      @Override
      public ProductDto update(ProductDto productDto, String productId) {

            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with given Id"));
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
            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with given Id"));
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
            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with given Id"));
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
}
