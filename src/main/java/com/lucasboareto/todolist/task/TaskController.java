package com.lucasboareto.todolist.task;

import com.lucasboareto.todolist.Repository.ITaskRepository;
import com.lucasboareto.todolist.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private ITaskRepository repository;
    private TaskService taskService;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        return taskService.createTask(taskModel, request);
    }

    @GetMapping("/")
    public List<TaskModel> getTaskList(HttpServletRequest request) {
        var idUser = (request.getAttribute("idUser"));
        return this.repository.findByIdUser((UUID) idUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        return taskService.updateTask(taskModel, request, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        return taskService.deleteTask(id);
    }
}
