package com.example.clientmicroserviceapplication.service;

import com.example.clientmicroserviceapplication.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.management.InstanceNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    public Cart getCartByUserId(Long id){
        ResponseEntity<Cart> cartResponse = restTemplate.getForEntity("http://CART-MICROSERVICE/cart/" + id, Cart.class);
        if (cartResponse.getStatusCode().is2xxSuccessful() && cartResponse.getBody() != null){
            return cartResponse.getBody();
        } else{
            return null;
        }
    }

    public Cart addCartItem(Long itemId, Long userId) throws InstanceNotFoundException{
        Cart cart = getCartByUserId(userId);
        if (cart == null){
            return null;
        }
        Map<String, Long> item = new HashMap<>();
        item.put("item-id",itemId);
        ResponseEntity<Cart> cartResponseEntity = restTemplate.postForEntity("http://CART-MICROSERVICE/cart/" + userId + "/?item-id={item-id}", null, Cart.class, item);
        if (cartResponseEntity.getStatusCode().is2xxSuccessful() && cartResponseEntity.getBody() != null){
            return cartResponseEntity.getBody();
        } else {
            return null;
        }
    }

    public void removeCartItem(Long cartItemId, Long userId){
        restTemplate.delete("http://CART-MICROSERVICE/cart/" + userId + "/" + cartItemId, null, Cart.class);
    }

    public void reduceCartItem(Long cartItemId, Long userId, Long amount){
        if (amount == 1){
            removeCartItem(cartItemId, userId);
        } else{
            Map<String, Long> params = new HashMap<>();
            params.put("userId", userId);
            params.put("cartItemId", cartItemId);
            params.put("amount", amount - 1);
            restTemplate.patchForObject("http://CART-MICROSERVICE/cart/{userId}/{cartItemId}?amount={amount}", null, Cart.class, params);
        }

    }
}
