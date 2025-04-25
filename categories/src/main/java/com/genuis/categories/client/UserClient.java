package com.genuis.categories.client;


import com.genuis.categories.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/api/v1/users")
public interface UserClient {
    @GetMapping("/{userId}")
    UserDTO getUserById(@PathVariable("userId") Long userId);
}