package com.api.ecommerce.orders.application;

import com.api.ecommerce.cart.domain.Cart;
import com.api.ecommerce.orders.domain.Order;
import com.api.ecommerce.orders.dto.response.MyOrderDTO;
import com.api.ecommerce.orders.dto.response.MyOrderDetailDTO;
import com.api.ecommerce.orders.dto.response.OrderDTO;
import com.api.ecommerce.orders.dto.response.OrderDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    Order createOrder(Long userId,Cart cart);
    Page<OrderDTO> getOrders(Pageable ordersPageable);
    List<OrderDetailDTO> getOrderDetails(Long orderId);
    Page<MyOrderDTO> getMyOrders(Long userId, Pageable pageRequest);
    List<MyOrderDetailDTO> getMyOrderDetails(Long orderId, Long aLong);
    void cancelMyPendingOrder(Long orderId,Long userId);
    Order getPendingOrder(Long orderId, Long userId);
}
