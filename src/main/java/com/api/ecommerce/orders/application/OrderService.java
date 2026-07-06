package com.api.ecommerce.orders.application;

import com.api.ecommerce.cart.domain.Cart;
import com.api.ecommerce.orders.domain.Order;
import com.api.ecommerce.orders.domain.OrderDetail;
import com.api.ecommerce.orders.domain.OrderStatus;
import com.api.ecommerce.orders.dto.response.MyOrderDTO;
import com.api.ecommerce.orders.dto.response.MyOrderDetailDTO;
import com.api.ecommerce.orders.dto.response.OrderDTO;
import com.api.ecommerce.orders.dto.response.OrderDetailDTO;
import com.api.ecommerce.orders.infrastructure.persistence.IOrderDetailRepository;
import com.api.ecommerce.orders.infrastructure.persistence.IOrderRepository;
import com.api.ecommerce.users.infrastructure.persistence.IAppUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService implements IOrderService{

    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository orderDetailRepository;

    public OrderService(IOrderRepository orderRepository, IOrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public Order createOrderFromCart(Cart cart) {
        Order order = new Order();
        List<OrderDetail> orderDetails = cart.getItems().stream()
                .map(cartItem ->
                        new OrderDetail(
                                null,
                                cartItem.getQuantity(),
                                cartItem.getProduct().getUnitPrice(),
                                cartItem.getProduct().getUnitPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())),
                                order,
                                cartItem.getProduct()
                        )
                )
                .toList();

        BigDecimal total = orderDetails.stream()
                .map(OrderDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);
        order.setOrderDetails(orderDetails);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUser(cart.getUser());
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order createOrder(Long userId, Cart cart) {
        if (orderRepository.findByUserIdAndStatus(userId, OrderStatus.PENDING).isPresent()) {
            throw new RuntimeException(
                    "You already have a pending order."
            );
        }
        return createOrderFromCart(cart);
    }
    @Override
    public Page<OrderDTO> getOrders(Pageable ordersPageable) {
        return orderRepository.findAllProjectedBy(ordersPageable);
    }

    @Override
    public List<OrderDetailDTO> getOrderDetails(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public Page<MyOrderDTO> getMyOrders(Long userId, Pageable pageRequest) {
        return orderRepository.findAllByUserId(userId,pageRequest);
    }

    @Override
    public List<MyOrderDetailDTO> getMyOrderDetails(Long orderId, Long userId) {
        return orderDetailRepository.findByOrderIdAndUserId(orderId,userId);
    }

    @Override
    public void cancelMyPendingOrder(Long orderId, Long userId) {
        Order orderRepo = this.getPendingOrder(orderId,userId);
        orderRepo.setStatus(OrderStatus.CANCELLED);
    }

    @Override
    public Order getPendingOrder(Long orderId, Long userId) {
        Order orderRepo = orderRepository.findByIdAndUserId(orderId,userId).orElseThrow();
        if (!orderRepo.getStatus().equals(OrderStatus.PENDING)){
            throw new RuntimeException("This order is not pending or not exists!");
        }
        return orderRepo;
    }
}
