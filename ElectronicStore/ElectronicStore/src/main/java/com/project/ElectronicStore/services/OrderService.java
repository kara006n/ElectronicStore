package com.project.ElectronicStore.services;

import com.project.ElectronicStore.dtos.CreateOrderRequest;
import com.project.ElectronicStore.dtos.OrderDto;
import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.dtos.UpdateOrderRequest;

import java.util.List;

public interface OrderService {

      //create an order
      public OrderDto createOrder(CreateOrderRequest createOrderRequest);
      //order remove
      void  removeOrder(String orderId);
      //get all order of user
      List<OrderDto> getOrdersOfUser(String userId);
      //get orders
      PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);
      //update order
      OrderDto updateOrder(UpdateOrderRequest createOrderRequest, String OrderId);
      //other methods
}
