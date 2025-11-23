package com.csu.orderservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.csu.orderservice.client.ItemClient;
import com.csu.orderservice.domain.po.Cart;
import com.csu.orderservice.domain.po.CartItem;
import com.csu.orderservice.domain.po.Item;
import com.csu.orderservice.mapper.CartDao;
import com.csu.orderservice.mapper.CartItemDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartDao cartDao;
    private final CartItemDao cartItemDao;
    @Autowired
    private final ItemClient itemClient;
    /**
     * 获取当前用户的购物车。
     * <p>
     * 如果用户的购物车不存在，则创建一个新的购物车并写入数据库。
     *
     * @param userId 当前用户ID
     * @return Cart 当前用户的购物车实体
     */
    public Cart getCartByUserId(Long userId) {
        Cart cart = cartDao.selectCartByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setTotalQuantity(0.00);
            cart.setSubTotal(0.00);
            int rows = cartDao.insertCart(cart);
            if (rows <= 0) {
                log.error("创建购物车失败，用户ID: {}", userId);
                throw new IllegalStateException("创建购物车失败");
            }
            log.info("成功创建购物车，用户ID: {}", userId);
        }
        return cart;
    }

    /**
     * 向购物车中添加商品或更新已存在商品的数量与总价。
     * @param userId   当前用户ID
     * @param itemId   商品ID
     * @param quantity 添加数量
     * @return Cart 更新后的购物车实体
     */
    @Transactional
    public Cart addItemToCart(Long userId, String itemId, Integer quantity) {
        Cart cart = getCartByUserId(userId);
        Item itemss=itemClient.getItemById(itemId);
        Double price = itemss.getPrice();
        // 查询购物车中的所有商品项
        List<CartItem> items = cartItemDao.selectCartItemsByCartId(cart.getCartId());
        if (items == null) {
            items = new ArrayList<>();
        }
        CartItem existingItem = items.stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // 如果商品已存在，更新数量和总价
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setTotalPrice(existingItem.getTotalPrice() + price * quantity);
            int updated = cartItemDao.updateCartItem(existingItem);
            if (updated <= 0) {
                log.error("更新购物车项失败，购物车ID: {}, 商品ID: {}", cart.getCartId(), itemId);
                throw new IllegalStateException("更新购物车项失败");
            }
        } else {
            // 如果商品不存在，新增购物车项
            CartItem newItem = new CartItem();
            newItem.setCartId(cart.getCartId());
            newItem.setItemId(itemId);
            newItem.setQuantity(quantity);
            newItem.setPrice(price);
            newItem.setTotalPrice(price * quantity);
            int inserted =  cartItemDao.insertCartItem(newItem);
            if (inserted <= 0) {
                log.error("新增购物车项失败，购物车ID: {}, 商品ID: {}", cart.getCartId(), itemId);
                throw new IllegalStateException("新增购物车项失败");
            }
        }
        // 重新计算并更新购物车合计数据
        recalcCart(cart);
        return cart;
    }

    /**
     * 更新购物车中指定商品的数量。
     * <p>
     * 如果更新后的数量为 0 或更小，则删除该购物车项。
     *
     * @param userId     当前用户ID
     * @param itemId     商品ID
     * @param newQuantity 更新后的数量
     * @return Cart 更新后的购物车实体
     */
    @Transactional
    public Cart updateItemQuantity(Long userId, String itemId, Integer newQuantity) {
        Cart cart = getCartByUserId(userId);
        Item itemss=itemClient.getItemById(itemId);
        Double price = itemss.getPrice();
        CartItem item = cartItemDao.selectCartItemByCartIdAndItemId(cart.getCartId(), itemId);
        if (item == null) {
            throw new NoSuchElementException("购物车中不存在该商品，商品ID：" + itemId);
        }
        if (newQuantity <= 0) {
            // 数量为 0 或负数时删除该购物车项
            int deleted = cartItemDao.deleteCartItemByCartIdAndItemId(cart.getCartId(), itemId);
            if (deleted <= 0) {
                log.error("删除购物车项失败，购物车ID: {}, 商品ID: {}", cart.getCartId(), itemId);
                throw new IllegalStateException("删除购物车项失败");
            }
        } else {
            // 更新数量和总价
            item.setQuantity(newQuantity);
            item.setTotalPrice(price * newQuantity);
            int updated = cartItemDao.updateCartItem(item);
            if (updated <= 0) {
                log.error("更新购物车项失败，购物车ID: {}, 商品ID: {}", cart.getCartId(), itemId);
                throw new IllegalStateException("更新购物车项失败");
            }
        }
        // 重新计算购物车合计数据
        recalcCart(cart);
        return cart;
    }

    /**
     * 从购物车中移除指定商品。
     *
     * @param userId 当前用户ID
     * @param itemId 要移除的商品ID
     * @return Cart 更新后的购物车实体
     */
    @Transactional
    public Cart removeItemFromCart(Long userId, String itemId) {
        Cart cart = getCartByUserId(userId);
        int deleted = cartItemDao.deleteCartItemByCartIdAndItemId(cart.getCartId(), itemId);
        if (deleted <= 0) {
            log.warn("购物车中未找到要删除的商品，购物车ID: {}, 商品ID: {}", cart.getCartId(), itemId);
        }
        // 重新计算购物车合计数据
        recalcCart(cart);
        return cart;
    }

    /**
     * 清空当前用户的购物车，删除所有购物车项，并重置合计数据。
     *
     * @param userId 当前用户ID
     */
    @Transactional
    public Cart clearCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        int deleted = cartItemDao.deleteCartItemsByCartId(cart.getCartId());
        if (deleted < 0) {
            log.error("清空购物车失败，购物车ID: {}", cart.getCartId());
            throw new IllegalStateException("清空购物车失败");
        }
        // 重置购物车合计数据
        cart.setTotalQuantity(0.00);
        cart.setSubTotal(0.00);
        int updated = cartDao.updateCart(cart);
        if (updated <= 0) {
            log.error("更新购物车数据失败，购物车ID: {}", cart.getCartId());
            throw new IllegalStateException("更新购物车数据失败");
        }
        log.info("成功清空购物车，购物车ID: {}", cart.getCartId());
        Cart null_cart=getCartByUserId(userId);
        return null_cart;
    }

    /**
     * 重新计算购物车中的总商品数量和小计金额，并更新购物车数据。
     * <p>
     * 此方法通过查询该购物车下所有购物车项进行计算。
     *
     * @param cart 当前购物车实体
     */
    private void recalcCart(Cart cart) {
        List<CartItem> items = cartItemDao.selectCartItemsByCartId(cart.getCartId());
        cart.setItems(items);
        if (items == null) {
            items = new ArrayList<>();
        }
        int totalQuantity = items.stream().mapToInt(CartItem::getQuantity).sum();
        double subTotal = items.stream().mapToDouble(CartItem::getTotalPrice).sum();
        cart.setTotalQuantity(totalQuantity);
        cart.setSubTotal(subTotal);
        int updated = cartDao.updateCart(cart);
        if (updated <= 0) {
            log.error("更新购物车合计数据失败，购物车ID: {}", cart.getCartId());
            throw new IllegalStateException("更新购物车合计数据失败");
        }
    }
}
