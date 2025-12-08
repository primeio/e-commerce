package com.shop.controller;

import com.shop.entity.Order;
import com.shop.entity.User;
import com.shop.service.OrderService;
import com.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String getOrder(Authentication auth, Model model){
        User user = userService.findByUsername(auth.getName())
                .orElseThrow();

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ADMIN"));

        if (isAdmin) {
            model.addAttribute("orders", orderService.getAllOrders());
            return "orders/list";
        }

        model.addAttribute("orders", orderService.findByUser(user));
        return "userOrders";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update-status/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam("status") Order.OrderStatus status
    ) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/orders";
    }


    @GetMapping("/{id}")
    public String viewOrder(@PathVariable long id, Authentication auth, Model model){
        User user = userService.findByUsername(auth.getName()).orElseThrow();
        Order order = orderService.getOrder(id);
        if (!order.getUser().getId().equals(user.getId())){
            throw new RuntimeException("access denied.");
        }
        model.addAttribute("order",order);
        model.addAttribute("orderItems", order.getOrderItems());
        return "/orders/details";
    }
    @PostMapping("/place")
    public String placeOrder(@RequestParam String shippingAddress,
                             @RequestParam String paymentMethod,
                             Authentication auth,
                             Model model){
        User user= userService.findByUsername(auth.getName()).orElseThrow();
        Order order = orderService.createOrderFromCart(user, shippingAddress, paymentMethod);
        return "/success";

    }
}
