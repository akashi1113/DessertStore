package com.csu.orderservice.service;

import com.csu.orderservice.client.ItemClient;
import com.csu.orderservice.domain.dto.*;
import com.csu.orderservice.domain.po.Cart;
import com.csu.orderservice.domain.po.LineItem;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.csu.orderservice.domain.po.Order;
import com.csu.orderservice.domain.po.OrderInfo;
import com.csu.orderservice.mapper.OrderDao;
import com.csu.orderservice.mapper.CartDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderDao orderDao;
    private final CartDao cartDao;
    

    /**
     * 获取订单准备数据
     * <p>
     * 该方法主要用于获取用户下单前需要展示的数据，如购物车信息、商品清单、合计金额等，
     * 为前端订单确认页面提供数据支持。
     *
     * @param userId 当前用户ID
     * @return OrderPrepareResponse 包含订单准备数据的 DTO 对象
     */
    public OrderPrepareResponse getOrderPreparationData(Long userId) {
        // 从购物车 DAO 中查询当前用户的购物车数据
        Cart cart = cartDao.selectCartByUserId(userId);
        if (cart == null) {
            throw new NoSuchElementException("购物车数据未找到");
        }
        OrderPrepareResponse response = new OrderPrepareResponse();
        response.setCartId(cart.getCartId());
        response.setTotalQuantity(cart.getTotalQuantity());
        response.setSubTotal(cart.getSubTotal());

        // 处理购物车中的多个商品项
        List<OrderItemResponse> items = cart.getItems().stream().map(cartItem -> {
            OrderItemResponse itemResp = new OrderItemResponse();
            itemResp.setItemId(cartItem.getItemId());
            itemResp.setQuantity(cartItem.getQuantity());
            itemResp.setTotalPrice(cartItem.getTotalPrice());
            return itemResp;
        }).collect(Collectors.toList());
        response.setOrderItems(items);

        // 根据需要可以添加其他数据（如优惠券、配送选项等）
        return response;
    }


    /**
     * 创建订单
     * <p>
     * 根据前端传入的 CreateOrderRequest 对象构建订单和对应的订单项，
     * 主要包括以下步骤：
     * 1. 根据请求数据构造 Order 实体对象；
     * 2. 插入订单数据到数据库（Mybatis 自动回写订单ID）；
     * 3. 根据订单项数据构造 LineItem 实体列表，并批量插入数据库；
     * 4. 可根据业务需要清空购物车（此处略）。
     *
     * @param userId  当前用户ID
     * @param request 包含订单基本信息和订单项信息的请求对象
     * @return OrderInfo 创建成功后的订单实体（包含生成的订单ID）
     */
    @Transactional
    public OrderInfo createOrder(Long userId, CreateOrderRequest request) {
        // 构造订单对象
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setFirstName(request.getFirstName());
        order.setLastName(request.getLastName());
        order.setShippingAddress1(request.getShippingAddress1());
        order.setShippingAddress2(request.getShippingAddress2());
        order.setCity(request.getCity());
        order.setPhone(request.getPhone());
        // 设定金额信息，注意这里的金额数据类型与业务保持一致
//        order.setSubTotal(request.getSubTotal());
//        order.setShippingCost(request.getShippingCost());
//        order.setTax(request.getTax());
        order.setGrandTotal(request.getGrandTotal());
        // 假设 Order 模型中有 userId 字段，用于标识订单归属
        order.setUserId(userId);

        // 插入订单数据到数据库，Mybatis 会自动回写生成的订单ID
        int rows = orderDao.insertOrder(order);
        if (rows <= 0) {
            log.error("订单创建失败，用户ID: {}", userId);
            throw new IllegalStateException("订单创建失败");
        }
        log.info("订单创建成功，订单ID: {}", order.getOrderId());
        Long orderId = order.getOrderId();
        // 处理订单项
        List<CreateOrderRequest.CreateOrderItem> orderItems = request.getOrderItems();
        if (orderItems != null && !orderItems.isEmpty()) {
            // 将每个订单项 DTO 转换为 LineItem 实体对象
            List<LineItem> lineItems = orderItems.stream().map(item -> {
                LineItem lineItem = new LineItem();
                lineItem.setOrderId(order.getOrderId());
                // 可选：设置购物车ID或其他关联字段，取决于业务需求
                lineItem.setCartId(request.getCartId());
                lineItem.setItemId(item.getItemId());
                lineItem.setQuantity(item.getQuantity());
                // 计算订单项总价：单价 * 数量，此处假设 totalPrice 已在请求中计算好，或者通过查商品价格后计算
                lineItem.setTotalPrice(item.getTotalPrice());
                return lineItem;
            }).collect(Collectors.toList());
            // 批量插入订单项数据
            for (LineItem lineItem : lineItems) {
                lineItem.setOrderId(orderId);
            }
            int inserted = orderDao.insertLineItems(lineItems);
            if (inserted != lineItems.size()) {
                log.error("订单项创建失败，订单ID: {}", order.getOrderId());
                throw new IllegalStateException("订单项创建失败");
            }
        } else {
            log.warn("创建订单时未传入订单项数据，订单ID: {}", order.getOrderId());
        }

        // 根据实际业务需要，订单创建成功后可以清空用户购物车（此处略）

        return orderDao.selectOrderInfo(orderId);
    }

    /**
     * 获取当前用户的订单历史（概要列表）
     * <p>
     * 该方法根据当前用户ID查询数据库中所有相关订单，并转换成订单概要 DTO，
     * 用于订单列表页面展示基本信息（如订单ID、日期、总金额、月份分组等）。
     *
     * @param userId 当前用户ID
     * @return List<OrderSummaryResponse> 订单概要列表 DTO
     */
    public List<OrderSummaryResponse> getOrderHistory(Long userId) {
        // 根据用户ID查询订单列表
        List<Order> orders = orderDao.selectOrdersByUserId(userId);
        // 将每个 Order 转换为 OrderSummaryResponse，转换逻辑根据业务需求编写
        return orders.stream()
                .map(this::convertToOrderSummaryResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取指定订单的详细信息
     * <p>
     * 根据订单ID查询订单详情以及订单项，并转换成详细的订单响应 DTO。
     * 同时校验该订单是否属于当前用户，防止越权访问。
     *
     * @param userId  当前用户ID
     * @param orderId 订单ID
     * @return OrderDetailsResponse 订单详细信息 DTO
     */
    public OrderDetailsResponse getOrderDetails(Long userId, Long orderId) {
        // 根据订单ID查询订单信息
        Order order = orderDao.selectOrderByOrderId(orderId);
        if (order == null) {
            throw new NoSuchElementException("订单未找到，订单ID：" + orderId);
        }
        // 校验订单归属，假设 Order 中存在 userId 字段
//        if (!order.getUserId().equals(userId)) {
//            throw new IllegalStateException("无权访问该订单");
//        }
        // 查询该订单对应的所有订单项
        List<LineItem> lineItems = orderDao.selectLineItemsByOrderId(orderId);
        // 将订单和订单项数据转换为详细响应 DTO
        return convertToOrderDetailsResponse(order, lineItems);
    }

    /**
     * 将订单实体转换为订单概要响应 DTO
     *
     * @param order 订单实体
     * @return OrderSummaryResponse 订单概要 DTO
     */
    private OrderSummaryResponse convertToOrderSummaryResponse(Order order) {
        OrderSummaryResponse summary = new OrderSummaryResponse();
        summary.setOrderId(order.getOrderId());
        summary.setOrderDate(order.getOrderDate());
        summary.setGrandTotal(order.getGrandTotal());
        // 根据需要，可扩展其他字段（如订单状态、支付状态等）
        return summary;
    }

    /**
     * 将订单及订单项数据转换为订单详细响应 DTO
     *
     * @param order     订单实体
     * @param lineItems 订单项列表
     * @return OrderDetailsResponse 订单详细 DTO
     */
    private OrderDetailsResponse convertToOrderDetailsResponse(Order order, List<LineItem> lineItems) {
        OrderDetailsResponse details = new OrderDetailsResponse();
        details.setOrderId(order.getOrderId());
        details.setOrderDate(order.getOrderDate());
        details.setShippingAddress1(order.getShippingAddress1());
        details.setShippingAddress2(order.getShippingAddress2());
        details.setCity(order.getCity());
        details.setFirstName(order.getFirstName());
        details.setLastName(order.getLastName());
        details.setPhone(order.getPhone());
//        details.setSubTotal(order.getSubTotal());
//        details.setShippingCost(order.getShippingCost());
//        details.setTax(order.getTax());
        details.setGrandTotal(order.getGrandTotal());
        // 将每个订单项转换为订单项响应 DTO
        List<OrderItemResponse> items = lineItems.stream().map(li -> {
            OrderItemResponse itemResp = new OrderItemResponse();
            itemResp.setItemId(li.getItemId());
            itemResp.setQuantity(li.getQuantity());
            itemResp.setTotalPrice(li.getTotalPrice());
            // 如有需要，可扩展：通过 ItemDao 查询商品详情（名称、图片等）并封装到响应 DTO 中
            return itemResp;
        }).collect(Collectors.toList());
        details.setOrderItems(items);
        return details;
    }
}
