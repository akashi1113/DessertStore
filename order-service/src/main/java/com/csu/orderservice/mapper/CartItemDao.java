package com.csu.orderservice.mapper;

import com.csu.orderservice.domain.po.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface CartItemDao {
    public List<CartItem> selectCartItemsByCartId(@Param("cartId") Long cartId);

    public int updateCartItem(CartItem existingItem);

    public int insertCartItem(CartItem newItem);

    public CartItem selectCartItemByCartIdAndItemId(@Param("cartId") Long cartId,@Param("itemId") String itemId);

    public int deleteCartItemByCartIdAndItemId(@Param("cartId") Long cartId,@Param("itemId") String itemId);

    public int deleteCartItemsByCartId(@Param("cartId") Long cartId);
}
