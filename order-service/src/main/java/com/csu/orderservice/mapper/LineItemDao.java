package com.csu.orderservice.mapper;

import com.csu.orderservice.domain.po.LineItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LineItemDao {
    /**
     * 批量插入订单项数据
     * @param lineItems 订单项列表
     * @return 插入成功的记录数
     */
    int insertLineItems(@Param("lineItems") List<LineItem> lineItems);

    /**
     * 根据订单ID查询对应的订单项
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<LineItem> selectLineItemsByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据 cartId 查询该购物车的所有购物车项
     */
    List<LineItem> selectLineItemsByCartId(Long cartId);

    /**
     * 根据 cartId 与 itemId 查询唯一的购物车项
     */
    LineItem selectLineItemByCartIdAndItemId(Long cartId, String itemId);

    /**
     * 插入购物车项
     */
    int insertLineItem(LineItem lineItem);

    /**
     * 更新购物车项（数量、总价）
     */
    int updateLineItem(LineItem lineItem);

    /**
     * 根据 cartId 与 itemId 删除指定购物车项
     */
    int deleteLineItemByCartIdAndItemId(Long cartId, String itemId);

    /**
     * 根据 cartId 删除所有购物车项（清空购物车）
     */
    int deleteLineItemsByCartId(Long cartId);
}

