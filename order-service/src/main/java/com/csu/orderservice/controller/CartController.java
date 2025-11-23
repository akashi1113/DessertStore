package com.csu.orderservice.controller;

import com.csu.orderservice.domain.po.CartItem;
import com.csu.orderservice.domain.dto.CartItemResponse;
import com.csu.orderservice.domain.dto.CartResponse;
import com.csu.orderservice.domain.dto.OrderAndCartResponse;
import com.csu.orderservice.domain.po.Cart;
import java.util.List;
import com.csu.orderservice.domain.dto.AccountResponse;
import org.springframework.http.ResponseEntity;
import java.util.stream.Collectors;

import com.csu.orderservice.service.CartService;
import com.csu.orderservice.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;
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

    /** 将 CartItem 转为只包含前端需要字段的 DTO */
    private CartItemResponse toDto(CartItem item) {
        CartItemResponse dto = new CartItemResponse();
        dto.setItemId(item.getItemId());
        dto.setQuantity(item.getQuantity());
        dto.setTotalPrice(item.getTotalPrice());
        return dto;
    }

    /** 将 Cart 转为 CartResponse，只暴露需要的字段 */
    private CartResponse toCartResponse(Cart cart) {
        CartResponse resp = new CartResponse();
        resp.setCartId(cart.getCartId());
//        resp.setUserId(cart.getUserId());
        resp.setTotalQuantity(cart.getTotalQuantity());
        resp.setSubTotal(cart.getSubTotal());
        // 只转换需要的字段
        List<CartItemResponse> dtoItems = cart.getItems().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        resp.setItems(dtoItems);
        return resp;
    }

    @GetMapping
    public ResponseEntity<OrderAndCartResponse<CartResponse>> getCart(
            @RequestHeader("Authorization") String tokenHeader) {
        Long userId = getUserIdFromToken(tokenHeader);
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(
                OrderAndCartResponse.createForSuccess(toCartResponse(cart))
        );
    }

    @PostMapping("/items/{itemId}")
    public ResponseEntity<OrderAndCartResponse<CartResponse>> addItemToCart(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("itemId") String itemId,
            @RequestParam("quantity") Integer quantity){
        Long userId = getUserIdFromToken(tokenHeader);
        Cart cart = cartService.addItemToCart(userId, itemId, quantity);
        return ResponseEntity.ok(
                OrderAndCartResponse.createForSuccess(toCartResponse(cart))
        );
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<OrderAndCartResponse<CartResponse>> updateItemQuantity(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("itemId") String itemId,
            @RequestParam("newquantity") Integer newquantity) {
        Long userId = getUserIdFromToken(tokenHeader);
        Cart cart = cartService.updateItemQuantity(userId, itemId, newquantity);
        return ResponseEntity.ok(
                OrderAndCartResponse.createForSuccess(toCartResponse(cart))
        );
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<OrderAndCartResponse<CartResponse>> removeItemFromCart(
            @RequestHeader("Authorization") String tokenHeader,
            @PathVariable("itemId") String itemId) {
        Long userId = getUserIdFromToken(tokenHeader);
        Cart cart = cartService.removeItemFromCart(userId, itemId);
        return ResponseEntity.ok(
                OrderAndCartResponse.createForSuccess(toCartResponse(cart))
        );
    }
    @DeleteMapping
    public ResponseEntity<OrderAndCartResponse<CartResponse>> clearCart(
            @RequestHeader("Authorization") String tokenHeader) {
        Long userId = getUserIdFromToken(tokenHeader);
        Cart cart=cartService.clearCart(userId);
        return ResponseEntity.ok(
                OrderAndCartResponse.createForSuccess("购物车已清空",toCartResponse(cart))
        );
    }
}
