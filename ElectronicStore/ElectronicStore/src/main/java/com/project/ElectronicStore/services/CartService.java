package com.project.ElectronicStore.services;

import com.project.ElectronicStore.dtos.AddItemToCartRequest;
import com.project.ElectronicStore.dtos.CartDto;

public interface CartService {

      //first add item to cart
      //case1: if user is adding some data to cart for first time then create cart and add item
      //case2: cart available then add item to cart

      CartDto addItemToCart(String userId, AddItemToCartRequest  request);//this method will also perform increment and decrement of no of items in cart
      //remove item from cart
      void removeItemFromCart(String userId, int cartItemId);
      //clear cart
      void clearCart(String userId);

      CartDto getCartByUser(String userId);
}
