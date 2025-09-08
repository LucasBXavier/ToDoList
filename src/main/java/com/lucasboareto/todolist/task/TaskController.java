package com.lucasboareto.todolist.task;

import com.lucasboareto.todolist.utils.utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private ITaskRepository repository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = (request.getAttribute("idUser"));
        taskModel.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();

        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início / data de termino deve ser maior que a data atual");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser menor que a data de termino");
        }
        var task = this.repository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> getTaskList(HttpServletRequest request) {
        var idUser = (request.getAttribute("idUser"));
        return this.repository.findByIdUser((UUID) idUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        var task = this.repository.findById(id).orElse(null);

        if(task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }

        var idUser = (request.getAttribute("idUser"));

        if(!task.getIdUser().equals(idUser)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Você não tem permissão para atualizar essa tarefa");
        }

        utils.copyNonNullProperties(taskModel, task);
        var taskUpdated = this.repository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }
}
