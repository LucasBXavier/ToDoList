package com.lucasboareto.todolist.user;

import com.lucasboareto.todolist.Repository.IUserRepository;
import com.lucasboareto.todolist.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;


    @Test
    void shouldCreateUserSuccessfully() {
        UserModel user = new UserModel();
        ResponseEntity expectedResponse = ResponseEntity.status(HttpStatus.CREATED).build();

        when(userService.createUser(any(UserModel.class))).thenReturn(expectedResponse);

        ResponseEntity response = userController.create(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void shouldReturnAllUsers() {
        UserModel user1 = new UserModel();
        UserModel user2 = new UserModel();
        List<UserModel> expectedList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(expectedList);

        List<UserModel> result = userController.getAll();

        assertEquals(2, result.size());
        assertEquals(expectedList, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        UUID id = UUID.randomUUID();
        ResponseEntity expectedResponse = ResponseEntity.ok().build();

        when(userService.deleteUser(id)).thenReturn(expectedResponse);

        ResponseEntity response = userController.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).deleteUser(id);
    }
}
