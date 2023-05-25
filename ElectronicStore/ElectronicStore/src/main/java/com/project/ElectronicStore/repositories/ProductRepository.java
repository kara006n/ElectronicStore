package com.project.ElectronicStore.repositories;

import com.project.ElectronicStore.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

      //search
      Page<Product> findByTitleContaining(String subTitle, Pageable pageable);
      Page<Product> findByLiveTrue(Pageable pageable);

      //can write other custom finder methods or query methods
}
