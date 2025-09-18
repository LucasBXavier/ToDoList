package com.lucasboareto.todolist.task;

import com.lucasboareto.todolist.Repository.ITaskRepository;
import com.lucasboareto.todolist.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private ITaskRepository repository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private TaskController taskController;


    @Test
    void shouldCreateTaskSuccessfully() {
        TaskModel task = new TaskModel();
        ResponseEntity expectedResponse = ResponseEntity.status(HttpStatus.CREATED).build();

        when(taskService.createTask(any(TaskModel.class), any(HttpServletRequest.class))).thenReturn(expectedResponse);

        ResponseEntity response = taskController.create(task, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(taskService, times(1)).createTask(task, request);
    }

    @Test
    void shouldReturnTaskList() {
        UUID userId = UUID.randomUUID();
        TaskModel task1 = new TaskModel();
        TaskModel task2 = new TaskModel();
        List<TaskModel> expectedList = Arrays.asList(task1, task2);

        when(request.getAttribute("idUser")).thenReturn(userId);
        when(repository.findByIdUser(userId)).thenReturn(expectedList);

        List<TaskModel> result = taskController.getTaskList(request);

        assertEquals(2, result.size());
        assertEquals(expectedList, result);
        verify(repository, times(1)).findByIdUser(userId);
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        UUID id = UUID.randomUUID();
        TaskModel task = new TaskModel();
        ResponseEntity expectedResponse = ResponseEntity.ok().build();

        when(taskService.updateTask(any(TaskModel.class), any(HttpServletRequest.class), eq(id))).thenReturn(expectedResponse);

        ResponseEntity response = taskController.update(task, request, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskService, times(1)).updateTask(task, request, id);
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        UUID id = UUID.randomUUID();
        ResponseEntity expectedResponse = ResponseEntity.noContent().build();

        when(taskService.deleteTask(id)).thenReturn(expectedResponse);

        ResponseEntity response = taskController.delete(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(id);
    }
}