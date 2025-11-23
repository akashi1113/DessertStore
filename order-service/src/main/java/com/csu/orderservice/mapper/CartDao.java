package com.csu.orderservice.mapper;

import com.csu.orderservice.domain.po.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CartDao {

    Cart selectCartByUserId(@Param("userId") Long userId);

    int insertCart(Cart cart);

    int updateCart(Cart cart);
}
