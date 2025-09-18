package com.lucasboareto.todolist.service;

import com.lucasboareto.todolist.Repository.IUserRepository;
import com.lucasboareto.todolist.user.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IUserRepository repository;

    @InjectMocks
    private UserService userService;

    private UserModel userModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userModel = new UserModel();
        userModel.setId(UUID.randomUUID());
        userModel.setUserName("lucas");
        userModel.setPassword("123456");
    }

    @Test
    void createUser_Success() {
        when(repository.findByUserName(userModel.getUserName())).thenReturn(null);
        when(repository.save(any(UserModel.class))).thenReturn(userModel);

        ResponseEntity response = userService.createUser(userModel);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Usuário: lucas criado com sucesso");

        verify(repository, times(1)).save(any(UserModel.class));
    }

    @Test
    void createUser_UserAlreadyExists() {
        when(repository.findByUserName(userModel.getUserName())).thenReturn(userModel);

        ResponseEntity response = userService.createUser(userModel);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(userModel);

        verify(repository, never()).save(any(UserModel.class));
    }

    @Test
    void deleteUser_Success() {
        UUID id = userModel.getId();
        when(repository.findById(id)).thenReturn(Optional.of(userModel));

        ResponseEntity response = userService.deleteUser(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Usuário deletado com sucesso");

        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void deleteUser_NotFound() {
        UUID id = userModel.getId();
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity response = userService.deleteUser(id);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("Usuário não encontrado");

        verify(repository, never()).deleteById(id);
    }
}