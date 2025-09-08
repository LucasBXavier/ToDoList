package com.lucasboareto.todolist.task;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskModelTest {
    @Test
    void shouldCreateTaskWithValidData() throws Exception {
        TaskModel task = new TaskModel();
        task.setId(UUID.randomUUID());
        task.setDescription("Complete the project");
        task.setTitle("Project");
        task.setStartAt(LocalDateTime.now());
        task.setEndAt(LocalDateTime.now().plusDays(1));
        task.setPriority("High");
        task.setIdUser(UUID.randomUUID());

        assertNotNull(task.getId());
        assertEquals("Complete the project", task.getDescription());
        assertEquals("Project", task.getTitle());
        assertNotNull(task.getStartAt());
        assertNotNull(task.getEndAt());
        assertEquals("High", task.getPriority());
        assertNotNull(task.getIdUser());
    }

    @Test
    void shouldSetTitleWhenLengthIsValid() throws Exception {
        TaskModel task = new TaskModel();
        task.setTitle("Valid Title");

        assertEquals("Valid Title", task.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenTitleExceedsMaxLength() {
        TaskModel task = new TaskModel();
        Exception exception = assertThrows(Exception.class, () -> task.setTitle("This title is way too long and exceeds the fifty character limit"));

        assertEquals("O título não pode ter mais que 50 caracteres", exception.getMessage());
    }
}