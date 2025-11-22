package com.csu.productservice.service;

import com.csu.productservice.dto.CommodityResponse;
import com.csu.productservice.entity.Item;
import com.csu.productservice.mapper.ItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service("itemService")
public class ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    @Autowired
    private ItemMapper itemMapper;

    // ========================================
    // 原有业务方法
    // ========================================

    public CommodityResponse<Item> searchItem(String itemId) {
        Item item = itemMapper.getItemById(itemId);
        if (item == null) {
            return CommodityResponse.createForSuccessMessage("没有该Item");
        }
        return CommodityResponse.createForSuccess(item);
    }

    public CommodityResponse<List<Item>> getItemListByProduct(String productId) {
        List<Item> items = itemMapper.selectByProduct(productId);
        if (items.isEmpty()) {
            return CommodityResponse.createForSuccessMessage("没有Item信息");
        }
        return CommodityResponse.createForSuccess(items);
    }

    public CommodityResponse<List<Item>> searchItemByKeywords(String keywords) {
        List<Item> items = itemMapper.selectListByKeyWords(keywords);
        if (items.isEmpty()) {
            return CommodityResponse.createForSuccessMessage("没有Item信息");
        }
        return CommodityResponse.createForSuccess(items);
    }


    /**
     * 扣减库存
     * @param itemId 商品项ID
     * @param quantity 扣减数量
     * @return 是否成功
     * @throws RuntimeException 库存不足时抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean reduceStock(String itemId, Integer quantity) {
        logger.info("扣减库存: itemId={}, quantity={}", itemId, quantity);

        // 参数校验
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("扣减数量必须大于0");
        }

        // 执行扣减（SQL中已包含库存检查）
        int rows = itemMapper.reduceStock(itemId, quantity);

        if (rows <= 0) {
            // 查询当前库存用于错误提示
            Item item = itemMapper.getItemById(itemId);
            if (item == null) {
                throw new RuntimeException("商品不存在: " + itemId);
            }

            logger.warn("库存不足: itemId={}, 需要={}, 当前={}",
                    itemId, quantity, item.getStock());

            throw new RuntimeException(
                    String.format("库存不足，需要%d件，当前仅有%d件",
                            quantity, item.getStock()));
        }

        logger.info("库存扣减成功: itemId={}, quantity={}", itemId, quantity);
        return true;
    }

    /**
     * 增加库存（订单取消时回滚）
     * @param itemId 商品项ID
     * @param quantity 增加数量
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseStock(String itemId, Integer quantity) {
        logger.info("增加库存: itemId={}, quantity={}", itemId, quantity);

        // 参数校验
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("增加数量必须大于0");
        }

        // 执行增加
        int rows = itemMapper.increaseStock(itemId, quantity);

        if (rows <= 0) {
            logger.error("库存增加失败: itemId={}", itemId);
            throw new RuntimeException("商品不存在: " + itemId);
        }

        logger.info("库存增加成功: itemId={}, quantity={}", itemId, quantity);
        return true;
    }

    /**
     * 检查库存是否充足
     * @param itemId 商品项ID
     * @param quantity 需要的数量
     * @return 是否充足
     */
    public boolean checkStock(String itemId, Integer quantity) {
        Item item = itemMapper.getItemById(itemId);

        if (item == null) {
            logger.warn("商品不存在: {}", itemId);
            return false;
        }

        Integer currentStock = item.getStock();
        boolean available = currentStock != null && currentStock >= quantity;

        logger.info("库存检查: itemId={}, 需要={}, 当前={}, 结果={}",
                itemId, quantity, currentStock, available);

        return available;
    }

    /**
     * 获取商品项当前库存
     */
    public Integer getStock(String itemId) {
        Item item = itemMapper.getItemById(itemId);
        if (item == null) {
            return null;
        }
        return item.getStock();
    }

    /**
     * 批量查询商品项
     */
    public List<Item> getItemsByIds(List<String> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            return Collections.emptyList();
        }
        return itemMapper.selectByIds(itemIds);
    }

    /**
     * 更新商品项
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateItem(Item item) {
        int rows = itemMapper.updateItem(item);
        return rows > 0;
    }

    /**
     * 添加商品项
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean insertItem(Item item) {
        int rows = itemMapper.insertItem(item);
        return rows > 0;
    }

    /**
     * 删除商品项
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteItem(String itemId) {
        int rows = itemMapper.deleteById(itemId);
        return rows > 0;
    }
}