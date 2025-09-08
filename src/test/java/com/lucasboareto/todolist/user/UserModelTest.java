package com.lucasboareto.todolist.user;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {

    UserModel user = new UserModel();


    @Test
    void shouldCreateUserWithValidData() {
        user.setId(UUID.randomUUID());
        user.setUserName("uniqueUser");
        user.setName("John Doe");
        user.setPassword("securePassword123");
        user.setCreatedAt(LocalDateTime.now());

        assertNotNull(user.getId());
        assertEquals("uniqueUser", user.getUserName());
        assertEquals("John Doe", user.getName());
        assertEquals("securePassword123", user.getPassword());
        assertNotNull(user.getCreatedAt());
    }
}