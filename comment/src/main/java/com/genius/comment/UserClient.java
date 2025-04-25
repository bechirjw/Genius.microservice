package com.genius.comment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", path = "/api/users")
public interface UserClient {
    @GetMapping("/{userId}")
    UserDTO getUserById(@PathVariable("userId") Long userId);
}