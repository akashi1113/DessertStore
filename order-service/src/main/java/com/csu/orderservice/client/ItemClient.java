package com.csu.orderservice.client;

import com.csu.orderservice.domain.po.Category;
import com.csu.orderservice.domain.po.Item;
import com.csu.orderservice.domain.po.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "product-service")
public interface ItemClient {
	@GetMapping("/feign/items/{itemId}")
	Item getItemById(@PathVariable String itemId) ;
	
	/**
	 * 扣减库存
	 */
	@PutMapping("/feign/items/{itemId}/reduce-stock")
	Map<String, Object> reduceStock(
			@PathVariable String itemId,
			@RequestParam Integer quantity);
	
	/**
	 * 回滚库存
	 */
	@PutMapping("/feign/items/{itemId}/restore-stock")
	Map<String, Object> restoreStock(
			@PathVariable String itemId,
			@RequestParam Integer quantity);
	/**
	 * 检查库存
	 */
	@GetMapping("/feign/items/{itemId}/check-stock")
	Map<String, Object> checkStock(
			@PathVariable String itemId,
			@RequestParam Integer quantity);
	
	// ========================================
	// Product（商品）相关接口
	// ========================================
	
	/**
	 * 根据ID获取商品信息
	 *
	 * @param productId 商品ID
	 * @return 商品实体
	 */
	@GetMapping("/feign/products/{productId}")
	Product getProductById(@PathVariable String productId);
	
	/**
	 * 根据分类ID获取商品列表
	 *
	 * @param categoryId 分类ID
	 * @return 商品列表
	 */
	@GetMapping("/feign/products/by-category/{categoryId}")
	public List<Product> getProductsByCategory(@PathVariable String categoryId);
	
	// ========================================
	// Category（分类）相关接口
	// ========================================
	
	/**
	 * 根据ID获取分类信息
	 *
	 * @param categoryId 分类ID
	 * @return 分类实体
	 */
	@GetMapping("/feign/categories/{categoryId}")
	Category getCategoryById(@PathVariable String categoryId);
	
	
}
