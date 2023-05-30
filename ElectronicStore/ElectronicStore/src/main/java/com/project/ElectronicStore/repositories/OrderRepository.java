package com.project.ElectronicStore.repositories;

import com.project.ElectronicStore.entities.Order;
import com.project.ElectronicStore.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

      List<Order> findByUser(User user);
}
