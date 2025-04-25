package com.genius.forum.service;

import com.genius.forum.model.User;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
}