package com.project.ElectronicStore.controllers;

import com.project.ElectronicStore.dtos.AddItemToCartRequest;
import com.project.ElectronicStore.dtos.ApiResponseMessage;
import com.project.ElectronicStore.dtos.CartDto;
import com.project.ElectronicStore.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

      Logger logger = LoggerFactory.getLogger(CartController.class);

      @Autowired
      private CartService cartService;

      //add item to cart
      @PostMapping("/{userId}")
      public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request){
            CartDto cartDto = cartService.addItemToCart(userId, request);
            //logger.info("returned");
            return  new ResponseEntity<>(cartDto, HttpStatus.OK);
      }

      @DeleteMapping("/{userId}/item/{itemId}")
      public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId, @PathVariable int itemId){
            cartService.removeItemFromCart(userId, itemId);
            ApiResponseMessage response = ApiResponseMessage.builder()
                    .message("Item is removed!!")
                    .success(true)
                    .status(HttpStatus.OK).build();
            return new ResponseEntity<>(response,HttpStatus.OK);
      }

      //clear cart
      @DeleteMapping("/{userId}")
      public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){
            cartService.clearCart(userId);
            ApiResponseMessage response = ApiResponseMessage.builder()
                    .message("Cart is cleared!!")
                    .success(true)
                    .status(HttpStatus.OK).build();
            return new ResponseEntity<>(response,HttpStatus.OK);
      }

      //get cart by user
      @GetMapping("/{userId}")
      public ResponseEntity<CartDto> getCart(@PathVariable String userId){
            CartDto cartDto = cartService.getCartByUser(userId);
            return  new ResponseEntity<>(cartDto, HttpStatus.OK);
      }
}

