package com.lucasboareto.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private ITaskRepository taskRepository;

    @InjectMocks
    private TaskController taskController;

    @Mock
    private HttpServletRequest request;

    @Test
    void createReturnsBadRequestWhenStartDateOrEndDateIsInThePast() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStartAt(LocalDateTime.now().minusDays(1));
        taskModel.setEndAt(LocalDateTime.now().plusDays(1));

        ResponseEntity response = taskController.create(taskModel, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("A data de início / data de termino deve ser maior que a data atual", response.getBody());
    }

    @Test
    void createReturnsBadRequestWhenStartDateIsAfterEndDate() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStartAt(LocalDateTime.now().plusDays(2));
        taskModel.setEndAt(LocalDateTime.now().plusDays(1));

        ResponseEntity response = taskController.create(taskModel, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("A data de início deve ser menor que a data de termino", response.getBody());
    }

    @Test
    void createReturnsOkWhenTaskIsCreatedSuccessfully() {
        TaskModel taskModel = new TaskModel();
        taskModel.setStartAt(LocalDateTime.now().plusDays(1));
        taskModel.setEndAt(LocalDateTime.now().plusDays(2));
        UUID idUser = UUID.randomUUID();

        Mockito.when(request.getAttribute("idUser")).thenReturn(idUser);
        Mockito.when(taskRepository.save(Mockito.any(TaskModel.class))).thenReturn(taskModel);

        ResponseEntity response = taskController.create(taskModel, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskModel, response.getBody());
        Mockito.verify(taskRepository).save(taskModel);
    }

    @Test
    void getTaskListReturnsTasksForUser() {
        UUID idUser = UUID.randomUUID();
        List<TaskModel> tasks = List.of(new TaskModel(), new TaskModel());

        Mockito.when(request.getAttribute("idUser")).thenReturn(idUser);
        Mockito.when(taskRepository.findByIdUser(idUser)).thenReturn(tasks);

        List<TaskModel> result = taskController.getTaskList(request);

        assertEquals(2, result.size());
        assertEquals(tasks, result);
    }

    @Test
    void updateReturnsNotFoundWhenTaskDoesNotExist() {
        UUID taskId = UUID.randomUUID();
        TaskModel taskModel = new TaskModel();

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResponseEntity response = taskController.update(taskModel, request, taskId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Tarefa não encontrada", response.getBody());
    }

    @Test
    void updateReturnsUnauthorizedWhenUserDoesNotOwnTask() {
        UUID taskId = UUID.randomUUID();
        UUID idUser = UUID.randomUUID();
        TaskModel existingTask = new TaskModel();
        existingTask.setIdUser(UUID.randomUUID());
        TaskModel taskModel = new TaskModel();

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        Mockito.when(request.getAttribute("idUser")).thenReturn(idUser);

        ResponseEntity response = taskController.update(taskModel, request, taskId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Você não tem permissão para atualizar essa tarefa", response.getBody());
    }

    @Test
    void updateReturnsOkWhenTaskIsUpdatedSuccessfully() {
        UUID taskId = UUID.randomUUID();
        UUID idUser = UUID.randomUUID();
        TaskModel existingTask = new TaskModel();
        existingTask.setIdUser(idUser);
        TaskModel taskModel = new TaskModel();

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        Mockito.when(request.getAttribute("idUser")).thenReturn(idUser);
        Mockito.when(taskRepository.save(existingTask)).thenReturn(existingTask);

        ResponseEntity response = taskController.update(taskModel, request, taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingTask, response.getBody());
        Mockito.verify(taskRepository).save(existingTask);
    }

}