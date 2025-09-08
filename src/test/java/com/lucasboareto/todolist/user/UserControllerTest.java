package com.lucasboareto.todolist.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    void createReturnsBadRequestWhenUserAlreadyExists() {
        UserModel existingUser = new UserModel();
        existingUser.setUserName("existingUser");
        Mockito.when(userRepository.findByUserName("existingUser")).thenReturn(existingUser);

        UserModel newUser = new UserModel();
        newUser.setUserName("existingUser");
        newUser.setPassword("password123");

        ResponseEntity response = userController.create(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Usuário já existe", response.getBody());
    }

    @Test
    void createReturnsOkWhenUserIsCreatedSuccessfully() {
        Mockito.when(userRepository.findByUserName("newUser")).thenReturn(null);

        UserModel newUser = new UserModel();
        newUser.setUserName("newUser");
        newUser.setPassword("password123");

        ResponseEntity response = userController.create(newUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário: newUser criado com sucesso", response.getBody());
        Mockito.verify(userRepository).save(Mockito.any(UserModel.class));
    }

    @Test
    void getAllReturnsListOfUsers() {
        List<UserModel> users = List.of(new UserModel(), new UserModel());
        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<UserModel> result = userController.getAll();

        assertEquals(2, result.size());
        assertEquals(users, result);
    }
}
