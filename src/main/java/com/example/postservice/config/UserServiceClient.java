package com.example.postservice.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "userService", url = "${user-service.url}")
public interface UserServiceClient {

    @GetMapping("")
    String getUserJson(@RequestHeader("Authorization") String token);
}
