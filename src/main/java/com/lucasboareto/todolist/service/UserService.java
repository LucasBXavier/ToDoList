package com.lucasboareto.todolist.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.lucasboareto.todolist.Repository.IUserRepository;
import com.lucasboareto.todolist.user.UserModel;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private IUserRepository repository;

    public ResponseEntity createUser(@RequestBody UserModel userModel){
        var user = this.repository.findByUserName(userModel.getUserName());
        if(user != null) {
            ResponseEntity.badRequest().body("Usuário já existe");
        }else{
            var passwordHash = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
            userModel.setPassword(passwordHash);
            this.repository.save(userModel);
            return ResponseEntity.ok().body("Usuário: " + userModel.getUserName() + " criado com sucesso");
        }
        return ResponseEntity.ok().body(user);
    }

    public ResponseEntity deleteUser(@PathVariable UUID id) {
        var user = this.repository.findById(id).orElse(null);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        this.repository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso");
    }
}
