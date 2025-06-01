package com.lucasboareto.todolist.task;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private ITaskRepository repository;

    @PostMapping("/cadastroTask")
    public ResponseEntity<String> create(@RequestBody TaskModel taskModel) {
        var task = repository.findByTitle(taskModel.getTitle());
        if(task != null) {
            throw new RuntimeException("Tarefa já existe.");
        }else {
            this.repository.save(taskModel);
            return ResponseEntity.ok().body("Tarefa: " + taskModel.getTitle() + " criada com sucesso");
        }
    }

    @GetMapping("/getTask")
    public TaskModel getTask() {
        var tasks = this.repository.findAll();
        return tasks.get(0);
    }
}
