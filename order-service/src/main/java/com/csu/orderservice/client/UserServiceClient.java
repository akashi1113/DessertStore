package com.csu.orderservice.client;

import com.csu.orderservice.domain.dto.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", path = "/accounts")
public interface UserServiceClient {
    @GetMapping
    ResponseEntity<AccountResponse<?>> getUserId(@RequestHeader("Authorization") String token);
}