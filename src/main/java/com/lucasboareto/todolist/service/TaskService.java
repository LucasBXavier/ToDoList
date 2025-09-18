package com.lucasboareto.todolist.service;

import com.lucasboareto.todolist.Repository.ITaskRepository;
import com.lucasboareto.todolist.task.TaskModel;
import com.lucasboareto.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TaskService {

    private ITaskRepository repository;

    public ResponseEntity createTask(@RequestBody TaskModel taskModel, HttpServletRequest request){
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

    public ResponseEntity updateTask(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){
        var task = this.repository.findById(id).orElse(null);

        if(task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }

        var idUser = (request.getAttribute("idUser"));

        if(!task.getIdUser().equals(idUser)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Você não tem permissão para atualizar essa tarefa");
        }

        Utils.copyNonNullProperties(taskModel, task);
        var taskUpdated = this.repository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }

    public ResponseEntity deleteTask(@PathVariable UUID id){
        var task = this.repository.findById(id).orElse(null);

        if(task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }

        this.repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Tarefa deletada com sucesso");
    }
}
