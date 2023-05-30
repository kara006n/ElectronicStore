package com.project.ElectronicStore.controllers;

import com.project.ElectronicStore.dtos.*;
import com.project.ElectronicStore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

      @Autowired
      private OrderService orderService;

      //create order
      @PostMapping
      public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request){
            OrderDto orderDto = orderService.createOrder(request);
            return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
      }
      //delete order
      @DeleteMapping("/{orderId}")
      public ResponseEntity<ApiResponseMessage> deleteOrder(@PathVariable String orderId){
            orderService.removeOrder(orderId);
            ApiResponseMessage response = ApiResponseMessage.builder()
                    .message("order is deleted")
                    .status(HttpStatus.OK)
                    .success(true).build();
            return new ResponseEntity<>(response, HttpStatus.OK);
      }
      //get orders of user
      @GetMapping("/users/{userId}")
      public ResponseEntity<List<OrderDto>> getOrder(@PathVariable String userId){
            List<OrderDto> orders = orderService.getOrdersOfUser(userId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
      }

      //get all orders
      @GetMapping
      public ResponseEntity<PageableResponse<OrderDto>> getAllOrder(
              @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
              @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
              @RequestParam(value = "sortBy", defaultValue = "orderedDate", required = false) String sortBy,
              @RequestParam(value = "sortDir", defaultValue = "desc", required = false) String sortDir
      ){
            PageableResponse<OrderDto> response = orderService.getOrders(pageNumber, pageSize, sortBy, sortDir);
            return new ResponseEntity<>(response, HttpStatus.OK);
      }
      //update order
      @PutMapping("/{orderId}")
      public ResponseEntity<OrderDto> updateOrder(@RequestBody UpdateOrderRequest request, @PathVariable String orderId){
            OrderDto orderDto = orderService.updateOrder(request,orderId);
            return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
      }

}
