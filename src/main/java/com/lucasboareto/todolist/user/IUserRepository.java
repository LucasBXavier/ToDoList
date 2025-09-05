package com.lucasboareto.todolist.user;

import org.springframework.data.jpa.repository.JpaRepository;



public interface IUserRepository extends JpaRepository<UserModel, String> {
    UserModel findByUserName(String userName);
}
