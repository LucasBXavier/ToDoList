package com.lucasboareto.todolist.Repository;

import com.lucasboareto.todolist.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByUserName(String userName);

    Optional<UserModel> findById(UUID id);
}
