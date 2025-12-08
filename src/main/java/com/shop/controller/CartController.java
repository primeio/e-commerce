package com.shop.controller;

import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.User;
import com.shop.service.CartService;
import com.shop.service.ProductService;
import com.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public String getCart(Authentication auth, Model model){
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        Cart cart = cartService.getOrCreateCart(user);
        List<CartItem> items = cartService.items(cart);
        BigDecimal totalPrice = items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        model.addAttribute("cart",cart);
        model.addAttribute("items",cartService.items(cart));
        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("totalItems",totalItems);
        return "cart";
    }

    @PostMapping("/addToCart/{id}")
    public String addTOCart(@PathVariable Long id,
                            @RequestParam(defaultValue = "1") Integer quantity,
                            Authentication auth){
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        System.out.println("user role:"+ user.getRoles());
        cartService.addToCart(user, id, quantity);
        return "redirect:/cart";
    }


    @PostMapping("/item/{id}/qty")
    public String updateQty(@PathVariable Long id, @RequestParam(name="qty", defaultValue="1") int qty){
        System.out.println("updateQty called: id=" + id + " qty=" + qty);
        cartService.updateQuantity(id, qty);
        return "redirect:/cart";
    }

    @PostMapping("/item/{id}/remove")
    public String remove(@PathVariable Long id){ cartService.remove(id); return
            "redirect:/cart"; }

    @GetMapping("/checkout")
    public String checkout(Authentication auth, Model model){
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        List<CartItem> cartItems = cartService.getCartItems(user);

        if (cartItems.isEmpty()){
            return "redirect:/cart";
        }
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("user",user);
        return "/checkout";
    }

}
