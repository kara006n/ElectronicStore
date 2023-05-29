package com.project.ElectronicStore.repositories;

import com.project.ElectronicStore.entities.Cart;
import com.project.ElectronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

      Optional<Cart> findByUser(User user);
}
