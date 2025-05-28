package com.lucasboareto.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private IUserRepository userRepository;

    @PostMapping("/cadastro")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var user = this.userRepository.findByUserName(userModel.getUserName());

        if(user != null) {
            return ResponseEntity.badRequest().body("Usuário já existe");
        }else{
            var passwordHash = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
            userModel.setPassword(passwordHash);
            this.userRepository.save(userModel);
            return ResponseEntity.ok().body("Usuário: " + userModel.getUserName() + " criado com sucesso");
        }
    }

    @GetMapping("/usuario")
    public ResponseEntity getAll() {
        var users = this.userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }
}
