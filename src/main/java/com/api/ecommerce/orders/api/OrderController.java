package com.api.ecommerce.orders.api;

import com.api.ecommerce.orders.application.CheckoutService;
import com.api.ecommerce.orders.application.IOrderService;
import com.api.ecommerce.orders.dto.response.PaymentCheckoutDTO;
import com.api.ecommerce.orders.dto.response.MyOrderDTO;
import com.api.ecommerce.orders.dto.response.MyOrderDetailDTO;
import com.api.ecommerce.orders.dto.response.StartCheckoutDTO;
import com.api.ecommerce.shared.security.jwt.JwtPrincipal;
import com.api.ecommerce.shared.web.PaginationConstants;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/order")
public class OrderController {

    private final IOrderService orderService;
    private final CheckoutService checkoutService;

    public OrderController(IOrderService orderService, CheckoutService checkoutService) {
        this.orderService = orderService;
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout/start")
    public ResponseEntity<StartCheckoutDTO> startCheckout(@AuthenticationPrincipal JwtPrincipal auth){
        return ResponseEntity.status(HttpStatus.OK)
                .body(checkoutService.startCheckout(auth.userId()));
    }
    
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<PaymentCheckoutDTO> payOrder(@PathVariable Long orderId, @AuthenticationPrincipal JwtPrincipal auth){
        return ResponseEntity.status(HttpStatus.OK)
                .body(checkoutService.payOrder(orderId, auth.userId()));
    }
    @GetMapping("/get-my-orders")
    public ResponseEntity<Page<MyOrderDTO>> getMyOrders(
            @PageableDefault(size = 8, page = 0) Pageable pageable,
            @AuthenticationPrincipal JwtPrincipal auth) throws BadRequestException {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageRequest = PaginationConstants.validatePageable(pageable.getPageNumber(),pageable.getPageSize(),sort);
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getMyOrders(auth.userId(),pageRequest));
    }

    @GetMapping("/get-my-order-details")
    public ResponseEntity<List<MyOrderDetailsDTO>> getMyOrderDetails(@RequestParam Long orderId, @AuthenticationPrincipal JwtPrincipal auth){
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getMyOrderDetails(orderId,auth.userId()));
    }

    @GetMapping("/get-my-pending-order")
    public ResponseEntity<MyOrderDTO> getMyPendingOrder(@AuthenticationPrincipal JwtPrincipal auth){
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getMyPendingOrder(auth.userId()));
    }
}
