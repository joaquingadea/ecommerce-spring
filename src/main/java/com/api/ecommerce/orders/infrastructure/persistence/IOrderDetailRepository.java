package com.api.ecommerce.orders.infrastructure.persistence;

import com.api.ecommerce.orders.domain.OrderDetail;
import com.api.ecommerce.orders.dto.response.MyOrderDetailDTO;
import com.api.ecommerce.orders.dto.response.OrderDetailDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    @Query("""
    SELECT
        od.quantity AS quantity,
        od.subtotal AS subtotal,
        od.unitPrice AS unitPrice,
        od.product.name AS productName
        FROM OrderDetail od
        WHERE od.order.id = :orderId
    """)
    List<OrderDetailDTO> findByOrderId(@Param("orderId")Long orderId);
    @Query("""
        SELECT  od.product.name AS productName, od.quantity AS quantity, od.subtotal AS subtotal, od.unitPrice AS unitPrice
        FROM OrderDetail od
        WHERE od.order.id = :orderId
        AND od.order.user.id = :userId
    """)
    List<MyOrderDetailDTO> findByOrderIdAndUserId(Long orderId, Long userId);
}
