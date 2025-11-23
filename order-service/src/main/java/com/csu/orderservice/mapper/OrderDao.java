package com.csu.orderservice.mapper;

import com.csu.orderservice.domain.po.Order;
import com.csu.orderservice.domain.po.OrderInfo;
import com.csu.orderservice.domain.po.LineItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderDao {
    /**
     * 插入订单数据
     * @param order 订单对象
     * @return 订单对象
     */
    int insertOrder(Order order);

    /**
     * 插入订单项数据
     * @param lineItems 订单项列表
     * @return 订单对象
     */
    int insertLineItems(List<LineItem> lineItems);

    /**
     * 根据订单 ID 查询订单信息
     * @param orderId 订单 ID
     * @return 订单信息对象
     */
    OrderInfo selectOrderInfo(Long orderId);

    /**
     * 根据用户 ID 查询订单列表
     * @param userId 用户 ID
     * @return 订单列表
     */
    List<Order> selectOrdersByUserId(@Param("userId") Long userId);

    /**
     * 根据订单 ID 查询订单详情
     * @param orderId 订单 ID
     * @return 订单对象
     */
    Order selectOrderByOrderId(@Param("orderId") Long orderId);

    List<LineItem> selectLineItemsByOrderId(Long orderId);
}

