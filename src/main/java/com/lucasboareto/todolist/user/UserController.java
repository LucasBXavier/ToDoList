package com.lucasboareto.todolist.user;

import com.lucasboareto.todolist.Repository.IUserRepository;
import com.lucasboareto.todolist.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private IUserRepository userRepository;
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        return userService.createUser(userModel);
    }
    @GetMapping("/")
    public List<UserModel> getAll() {
        return this.userRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        return userService.deleteUser(id);
    }

}
