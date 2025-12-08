package com.shop.service;

import com.shop.entity.CartItem;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.entity.User;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final CartService cartService;

    @Transactional
    public Order createOrderFromCart(User user, String shippingAddress, String paymentMethod) {
    List<CartItem> cartItems = cartService.getCartItems(user);

    if(cartItems.isEmpty()){
        throw new RuntimeException("Can not create order, cart is empty.");
    }
    Order order = new Order();
    order.setUser(user);
    order.setPaymentMethod(paymentMethod);
    order.setShippingAddress(shippingAddress);
    order.setStatus(Order.OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem: cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.addOrderItem(orderItem);

            totalAmount = totalAmount.add(cartItem.getTotalPrice());
        }
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(user);
        return savedOrder;

        }
     @Transactional
    public Order updatePaymentStatus(long orderId, String paymentStatus){
     Order order = orderRepository.findById(orderId).orElseThrow(()->
             new RuntimeException("Order not found"));

     order.setPaymentStatus(paymentStatus);

     if("PAID".equals(paymentStatus)){
         order.setStatus(Order.OrderStatus.CONFIRMED);
     }
     return orderRepository.save(order);
     }

     @Transactional
    public void cancelOrder(long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(()->
                new RuntimeException("Order not found"));

        if (order.getStatus()==Order.OrderStatus.SHIPPED ||
        order.getStatus()== Order.OrderStatus.DELIVERED){
            throw new RuntimeException("Can not cancel order");

        }
     order.setStatus(Order.OrderStatus.CANCELLED);
     }
     public List<OrderItem> getOrderItems(Long id){
        return orderItemRepository.findByOrderId(id);
     }
     public Order getOrder(long id){
        return orderRepository.findById(id).orElseThrow();
     }
    public List<Order> findByUser(User user) {
        return orderRepository.findByUser(user);
    }
    public List<Order> getAllOrders(){return orderRepository.findAll();}
    public void updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        orderRepository.save(order);
    }

}
