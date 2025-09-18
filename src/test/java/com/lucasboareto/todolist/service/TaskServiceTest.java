package com.lucasboareto.todolist.service;

import com.lucasboareto.todolist.Repository.ITaskRepository;
import com.lucasboareto.todolist.task.TaskModel;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private ITaskRepository repository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private TaskService taskService;

    private UUID userId;

    private TaskModel taskModel;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();

        taskModel = new TaskModel();
        taskModel.setId(UUID.randomUUID());
        taskModel.setTitle("Test Task");
        taskModel.setIdUser(userId);
        taskModel.setStartAt(LocalDateTime.now().plusDays(1));
        taskModel.setEndAt(LocalDateTime.now().plusDays(2));
    }

    @Test
    void createTask_Success() {
        when(request.getAttribute("idUser")).thenReturn(userId);
        when(repository.save(any(TaskModel.class))).thenReturn(taskModel);

        ResponseEntity response = taskService.createTask(taskModel, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(TaskModel.class);
    }

    @Test
    void createTask_StartDateInPast() {
        taskModel.setStartAt(LocalDateTime.now().minusDays(1));
        when(request.getAttribute("idUser")).thenReturn(userId);

        ResponseEntity response = taskService.createTask(taskModel, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("A data de início / data de termino deve ser maior que a data atual");
    }

    @Test
    void createTask_EndDateInPast() {
        taskModel.setEndAt(LocalDateTime.now().minusDays(1));
        when(request.getAttribute("idUser")).thenReturn(userId);

        ResponseEntity response = taskService.createTask(taskModel, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("A data de início / data de termino deve ser maior que a data atual");
    }

    @Test
    void createTask_StartAfterEnd() {
        taskModel.setStartAt(LocalDateTime.now().plusDays(5));
        taskModel.setEndAt(LocalDateTime.now().plusDays(1));
        when(request.getAttribute("idUser")).thenReturn(userId);

        ResponseEntity response = taskService.createTask(taskModel, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
        assertThat(response.getBody()).isEqualTo("A data de início deve ser menor que a data de termino");
    }

    @Test
    void updateTask_Success() {
        when(repository.findById(taskModel.getId())).thenReturn(Optional.of(taskModel));
        when(request.getAttribute("idUser")).thenReturn(userId);
        when(repository.save(any(TaskModel.class))).thenReturn(taskModel);

        ResponseEntity response = taskService.updateTask(taskModel, request, taskModel.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(TaskModel.class);
    }

    @Test
    void updateTask_TaskNotFound() {
        when(repository.findById(taskModel.getId())).thenReturn(Optional.empty());

        ResponseEntity response = taskService.updateTask(taskModel, request, taskModel.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("Tarefa não encontrada");
    }

    @Test
    void updateTask_UnauthorizedUser() {
        when(repository.findById(taskModel.getId())).thenReturn(Optional.of(taskModel));
        when(request.getAttribute("idUser")).thenReturn(UUID.randomUUID());

        ResponseEntity response = taskService.updateTask(taskModel, request, taskModel.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
        assertThat(response.getBody()).isEqualTo("Você não tem permissão para atualizar essa tarefa");
    }

    @Test
    void deleteTask_Success() {
        when(repository.findById(taskModel.getId())).thenReturn(Optional.of(taskModel));

        ResponseEntity response = taskService.deleteTask(taskModel.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Tarefa deletada com sucesso");
        verify(repository, times(1)).deleteById(taskModel.getId());
    }

    @Test
    void deleteTask_NotFound() {
        when(repository.findById(taskModel.getId())).thenReturn(Optional.empty());

        ResponseEntity response = taskService.deleteTask(taskModel.getId());

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getBody()).isEqualTo("Tarefa não encontrada");
    }

}