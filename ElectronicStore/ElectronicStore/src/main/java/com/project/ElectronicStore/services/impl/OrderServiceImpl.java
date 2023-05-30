package com.project.ElectronicStore.services.impl;

import com.project.ElectronicStore.dtos.CreateOrderRequest;
import com.project.ElectronicStore.dtos.OrderDto;
import com.project.ElectronicStore.dtos.PageableResponse;
import com.project.ElectronicStore.dtos.UpdateOrderRequest;
import com.project.ElectronicStore.entities.*;
import com.project.ElectronicStore.exception.BadApiRequestException;
import com.project.ElectronicStore.exception.ResourceNotFoundException;
import com.project.ElectronicStore.helper.Helper;
import com.project.ElectronicStore.repositories.CartRepository;
import com.project.ElectronicStore.repositories.OrderRepository;
import com.project.ElectronicStore.repositories.UserRepository;
import com.project.ElectronicStore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

      @Autowired
      private CartRepository cartRepository;

      @Autowired
      private UserRepository userRepository;

      @Autowired
      private OrderRepository orderRepository;

     @Autowired
      private ModelMapper modelMapper;

     @Override
      public OrderDto createOrder(CreateOrderRequest createOrderRequest) {

           //fetch user
           User user = userRepository.findById(createOrderRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("user not found with given id"));
           Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart not found for user"));
           List<CartItem> cartItems = cart.getItems();
           if(cartItems.size()<=0){
                 throw new BadApiRequestException("Invalid number of items in cart");
           }

           Order order = Order.builder()
                   .billingName(createOrderRequest.getBillingName())
                   .billingPhone(createOrderRequest.getBillingPhone())
                   .billingAddress(createOrderRequest.getBillingAddress())
                   .orderedDate(new Date())
                   .deliveryDate(null)
                   .paymentStatus(createOrderRequest.getPaymentStatus())
                   .orderStatus(createOrderRequest.getOrderStatus())
                   .orderId(UUID.randomUUID().toString())
                   .user(user).build();
           //order item , amount not set
           AtomicReference<Double> orderAmount = new AtomicReference(0.0);
           List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
                 //cartItem->orderItem
                 OrderItem orderItem = OrderItem.builder()
                         .quantity(cartItem.getQuantity())
                         .totalPrice(cartItem.getTotalPrice())
                         .order(order)
                         .product(cartItem.getProduct()).build();
                 orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
                 return orderItem;
           }).collect(Collectors.toList());
           order.setOrderAmount(orderAmount.get());
           order.setOrderItem(orderItems);
            cart.getItems().clear();
            cartRepository.save(cart);
           Order savedOrder = orderRepository.save(order);
           return modelMapper.map(savedOrder, OrderDto.class);
     }

      @Override
      public void removeOrder(String orderId) {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            orderRepository.delete(order);
            //automatically all order items will be deleted as cascade all is implemented in entity
      }

      @Override
      public List<OrderDto> getOrdersOfUser(String userId) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with given id"));
            List<Order> orders = orderRepository.findByUser(user);
            List<OrderDto> ordersDto = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
            return ordersDto;
      }

      @Override
      public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
            Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
            Page<Order> page = orderRepository.findAll(pageable);
            return Helper.getPageableResponse(page, OrderDto.class);
      }

      @Override
      public OrderDto updateOrder(UpdateOrderRequest createOrderRequest, String orderId) {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order with given id not found"));
            order.setOrderStatus(createOrderRequest.getOrderStatus());
            order.setDeliveryDate(createOrderRequest.getDeliveryDate());
            order.setPaymentStatus(createOrderRequest.getPaymentStatus());
            Order updatedOrder = orderRepository.save(order);
            return modelMapper.map(updatedOrder, OrderDto.class);
      }
}
