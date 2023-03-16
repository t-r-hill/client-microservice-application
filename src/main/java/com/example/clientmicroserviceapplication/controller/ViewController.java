package com.example.clientmicroserviceapplication.controller;

import com.example.clientmicroserviceapplication.model.*;
import com.example.clientmicroserviceapplication.service.CartService;
import com.example.clientmicroserviceapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.clientmicroserviceapplication.service.ItemService;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Controller
public class ViewController {

    @Autowired
    ItemService itemService;

    @Autowired
    CartService cartService;

    @Autowired
    UserService userService;

    @GetMapping("/items")
    public String displayItems(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        return "item-list";
    }

    @GetMapping("/cart")
    public String displayCart(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Cart cart = cartService.getCartByUserId(userDetails.getId());
        model.addAttribute("cartItems", itemService.getItemsFromCart(cart));
        return "cart";
    }

    @PostMapping("/cart/add/{itemId}")
    public String addItemToCart(@PathVariable Long itemId,
                                Authentication authentication){
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Cart cart = cartService.addCartItem(itemId, userDetails.getId());
            return "redirect:/cart";
        } catch (InstanceNotFoundException e) {
            return "error";
        }
    }

    @GetMapping("/cart/decrease/{cartItemId}")
    public String reduceItemFromCart(@PathVariable Long cartItemId,
                                       @RequestParam Long amount,
                                       Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        cartService.reduceCartItem(cartItemId, userDetails.getId(), amount);
        return "redirect:/cart";
    }

    @GetMapping("/cart/remove/{cartItemId}")
    public String removeItemFromCart(@PathVariable Long cartItemId,
                                Authentication authentication,
                                Model model){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        cartService.removeCartItem(cartItemId, userDetails.getId());
        return "redirect:/cart";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model){
        User user = User.builder().build();
        model.addAttribute("user", user);
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        User user = User.builder().build();
        model.addAttribute("user", user);
        return "register";
    }

    @GetMapping("/register/success")
    public String showRegistrationSuccessPage(){
        return "register-success";
    }

    @GetMapping("/register/failed")
    public String showRegistrationFailurePage(){
        return "register-failure";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute User user){
        try{
            userService.createNewUser(user);
            return "redirect:/register/success";
        } catch (Exception e){
            return "redirect:/register/failure";
        }

    }
}
