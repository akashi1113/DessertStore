package com.csu.analyticsservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "user-service", path = "/accounts")
public interface UserServiceClient {

    /**
     * 获取用户信息
     * 对应：GET /accounts/{userid}
     */
    @GetMapping("/{userid}")
    Map<String, Object> getAccount(
            @RequestHeader("Authorization") String token,
            @PathVariable Long userid
    );
}
