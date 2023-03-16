package com.example.clientmicroserviceapplication.service;

import com.example.clientmicroserviceapplication.model.Cart;
import com.example.clientmicroserviceapplication.model.CartItem;
import com.example.clientmicroserviceapplication.model.Item;
import javassist.compiler.ast.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ItemService {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    public Item getItemById(Long id){
        ResponseEntity<Item> response = restTemplate.getForEntity("http://ITEM-MICROSERVICE/item/" + id, Item.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null){
            return response.getBody();
        } else{
            return null;
        }
    }

    public List<Item> getAllItems() {
        ResponseEntity<Item[]> response = restTemplate.getForEntity("http://ITEM-MICROSERVICE/item", Item[].class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        } else {
            return null;
        }
    }

    public List<CartItem> getItemsFromCart(Cart cart){
        Map<Long, Item> items = getAllItems().stream().collect(Collectors.toMap(Item::getId, Function.identity()));
        if (cart.getItems() == null){
            return new ArrayList<>();
        }
        cart.getItems().forEach(item -> item.setItem(items.get(item.getItemId())));
        return cart.getItems();

//        allItems.stream()
//                .flatMap(item -> cart.getItems()
//                        .stream()
//                        .filter(cartItem -> cartItem.getItemId() == item.getId()).map(cartItem -> cartItem.setItem(item))
//        return itemAmounts;
    }
}
