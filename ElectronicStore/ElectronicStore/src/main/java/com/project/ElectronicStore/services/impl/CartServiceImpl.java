package com.project.ElectronicStore.services.impl;

import com.project.ElectronicStore.dtos.AddItemToCartRequest;
import com.project.ElectronicStore.dtos.CartDto;
import com.project.ElectronicStore.entities.Cart;
import com.project.ElectronicStore.entities.CartItem;
import com.project.ElectronicStore.entities.Product;
import com.project.ElectronicStore.entities.User;
import com.project.ElectronicStore.exception.BadApiRequestException;
import com.project.ElectronicStore.exception.ResourceNotFoundException;
import com.project.ElectronicStore.repositories.CartItemRepository;
import com.project.ElectronicStore.repositories.CartRepository;
import com.project.ElectronicStore.repositories.ProductRepository;
import com.project.ElectronicStore.repositories.UserRepository;
import com.project.ElectronicStore.services.CartService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

      Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

      @Autowired
      private ProductRepository productRepository;

      @Autowired
      private UserRepository userRepository;

      @Autowired
      private CartRepository cartRepository;

      @Autowired
      private ModelMapper mapper;

      @Autowired
      private CartItemRepository cartItemRepository;

      @Override
      public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

            int quantity = request.getQuantity();
            String productId = request.getProductId();

            if(quantity<=0){
                  throw new BadApiRequestException("Requested quantity is not valid");
            }
            //fetch the product
            Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found in database"));
            //fetch the user
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database"));
            Cart cart = null;
            try{
                  cart = cartRepository.findByUser(user).get();
                  logger.info("inside cart creation");
            }catch(NoSuchElementException e){
                  cart = new Cart();
                  cart.setCartId(UUID.randomUUID().toString());
                  cart.setCreatedDate(new Date());
            }

            //perform cart operation
            List<CartItem> items = cart.getItems();
            //if cart item already present then update quantity
            AtomicReference<Boolean> updated =new AtomicReference<>(false);
            List<CartItem> updatedItems = items.stream().map(item -> {
                  if (item.getProduct().getProductId().equals(productId)) {
                        //item already present in cart
                        item.setQuantity(quantity);
                        item.setTotalPrice(quantity * product.getDiscountedPrice());
                        updated.set(true);
                  }
                  return item;
            }).collect(Collectors.toList());
            cart.setItems(updatedItems);
            //create items
            if(!updated.get()){
                  CartItem cartItem = CartItem.builder()
                          .quantity(quantity)
                          .totalPrice(quantity * product.getDiscountedPrice())
                          .cart(cart)
                          .product(product).build();
                  items.add(cartItem);
                  cart.setItems(items);
                  logger.info("inside cartItem creation for new user");
            }

            cart.setUser(user);
            Cart updatedCart = cartRepository.save(cart);
            logger.info("finally done");
            return mapper.map(updatedCart, CartDto.class);

      }

      @Override
      public void removeItemFromCart(String userId, int cartItemId) {
            //condition
            CartItem cartItem1 = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart Item not found"));
            cartItemRepository.delete(cartItem1);
      }

      @Override
      public void clearCart(String userId) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database"));
            Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart of user not found"));
            cart.getItems().clear();
            cartRepository.save(cart);
      }

      @Override
      public CartDto getCartByUser(String userId) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found in database"));
            Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("cart of user not found"));
            return mapper.map(cart, CartDto.class);
      }
}
