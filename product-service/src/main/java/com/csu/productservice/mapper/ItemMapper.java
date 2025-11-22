package com.csu.productservice.mapper;

import com.csu.productservice.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper {

    /**
     * 根据ID查询商品项
     */
    Item getItemById(String itemId);

    /**
     * 根据商品ID查询商品项列表
     */
    List<Item> selectByProduct(@Param("productId") String productId);

    /**
     * 根据关键词搜索
     */
    List<Item> selectListByKeyWords(@Param("keyword") String keyword);

    // ========================================
    // 原有删除方法
    // ========================================

    /**
     * 根据ID删除商品项
     */
    int deleteById(@Param("itemId") String itemId);

    /**
     * 扣减库存（使用乐观锁防止超卖）
     * @param itemId 商品项ID
     * @param quantity 扣减数量
     * @return 影响的行数（0表示库存不足或商品不存在）
     */
    int reduceStock(@Param("itemId") String itemId,
                    @Param("quantity") Integer quantity);

    /**
     * 增加库存（订单取消时回滚）
     * @param itemId 商品项ID
     * @param quantity 增加数量
     * @return 影响的行数
     */
    int increaseStock(@Param("itemId") String itemId,
                      @Param("quantity") Integer quantity);


    /**
     * 批量查询商品项
     * @param itemIds 商品项ID列表
     * @return 商品项列表
     */
    List<Item> selectByIds(@Param("itemIds") List<String> itemIds);

    /**
     * 更新商品项信息
     * @param item 商品项实体
     * @return 影响的行数
     */
    int updateItem(Item item);

    /**
     * 插入商品项
     * @param item 商品项实体
     * @return 影响的行数
     */
    int insertItem(Item item);
}