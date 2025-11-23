package com.csu.orderservice.controller;

import com.csu.orderservice.domain.dto.CreateOrderRequest;
import com.csu.orderservice.domain.dto.OrderAndCartResponse;
import com.csu.orderservice.domain.dto.OrderDetailsResponse;
import com.csu.orderservice.domain.dto.OrderPrepareResponse;
import com.csu.orderservice.domain.dto.OrderSummaryResponse;
import com.csu.orderservice.domain.po.OrderInfo;
import com.csu.orderservice.client.UserServiceClient;
import com.csu.orderservice.service.OrderService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import com.csu.orderservice.domain.dto.AccountResponse;
import java.util.Map;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final UserServiceClient userServiceClient;

      private Long getUserIdFromToken(String tokenHeader) {
            if (tokenHeader == null || tokenHeader.isEmpty()) {
                throw new IllegalArgumentException("缺少或无效的 Authorization 标头。");
            }
            ResponseEntity<AccountResponse<?>> resp = userServiceClient.getUserId(tokenHeader);
            Object data = resp.getBody() != null ? resp.getBody().getData() : null;
            if (data == null) {
                throw new IllegalArgumentException("用户未认证或 token 无效");
            }
            if (data instanceof Number) {
                return ((Number) data).longValue();
            }
            return Long.valueOf(String.valueOf(data));
        }

        /**
         * GET /orders/prepare - 获取订单准备数据
         * 成功: 200 OK, Body: OrderAndCartResponse<OrderPrepareResponse>
         * 失败: 401, 404, 400 (由 GlobalExceptionHandler 处理)
         */
        @GetMapping("/prepare")
        public ResponseEntity<OrderAndCartResponse<OrderPrepareResponse>> getOrderPreparation(
                @RequestHeader("Authorization") String tokenHeader) {
            Long userId = getUserIdFromToken(tokenHeader);
            OrderPrepareResponse prepareData = orderService.getOrderPreparationData(userId);
            return ResponseEntity.ok(OrderAndCartResponse.createForSuccess(prepareData));
        }

        /**
         * POST /orders - 创建订单
         * 成功: 201 Created, Body: OrderAndCartResponse<Map<String, Long>> (包含 orderId)
         * 失败: 401, 404, 400, 409, 500 (由 GlobalExceptionHandler 处理)
         */
        @PostMapping
        public ResponseEntity<OrderAndCartResponse<Map<String, Object>>> createOrder(
                @RequestHeader("Authorization") String tokenHeader,
                @Valid @RequestBody CreateOrderRequest request) {
            Long userId = getUserIdFromToken(tokenHeader);
            OrderInfo createdOrder = orderService.createOrder(userId, request);
            Map<String, Object> responseData = Map.of(
                    "orderId", createdOrder.getOrderId(),
                    "orderDate", createdOrder.getOrderDate(),
                    "grandTotal", createdOrder.getGrandTotal()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(OrderAndCartResponse.createForSuccess(responseData));
        }

        /**
         * GET /orders - 获取当前用户的订单历史 (概要列表)
         * 成功: 200 OK, Body: OrderAndCartResponse<List<OrderSummaryResponse>>
         * 失败: 401 (由 GlobalExceptionHandler 处理)
         */
        @GetMapping
        public ResponseEntity<OrderAndCartResponse<List<OrderSummaryResponse>>> getOrderHistory(
                @RequestHeader("Authorization") String tokenHeader) {
            Long userId = getUserIdFromToken(tokenHeader);
            List<OrderSummaryResponse> history = orderService.getOrderHistory(userId);
            return ResponseEntity.ok(OrderAndCartResponse.createForSuccess(history));
        }

        /**
         * GET /orders/{orderId} - 获取指定订单的详情
         * 成功: 200 OK, Body: OrderAndCartResponse<OrderDetailsResponse>
         * 失败: 401, 403, 404 (由 GlobalExceptionHandler 处理)
         */
        @GetMapping("/{orderId}")
        public ResponseEntity<OrderAndCartResponse<OrderDetailsResponse>> getOrderDetails(
                @RequestHeader("Authorization") String tokenHeader,
                @PathVariable Long orderId) {
            Long userId = getUserIdFromToken(tokenHeader);
            OrderDetailsResponse details = orderService.getOrderDetails(userId, orderId);
            return ResponseEntity.ok(OrderAndCartResponse.createForSuccess(details));
        }
//    @PostMapping("/insertOrder")
//    public OrderInfo insertOrder(@RequestBody InsertOrderRequest request) {
//        Order order = request.getOrder();
//        List<LineItem> lineItems = request.getLineItems();
//        return orderService.insertOrderAndGetInfo(order, lineItems);
//    }
}