package com.api.ecommerce.orders.infrastructure.persistence;

import com.api.ecommerce.orders.domain.Order;
import com.api.ecommerce.orders.domain.OrderStatus;
import com.api.ecommerce.orders.dto.response.MyOrderDTO;
import com.api.ecommerce.orders.dto.response.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByUserIdAndStatus(Long userId, OrderStatus status);
    Page<OrderDTO> findAllProjectedBy(Pageable ordersPageable);
    Page<MyOrderDTO> findAllByUserId(Long userId,Pageable pageable);
    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
