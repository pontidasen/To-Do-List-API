package com.todolist.todolist.dto;

import lombok.Data;

//Format for response user when user Login
@Data
public class LoginDto {
    private String usernameOrEmail;
    private String password;
}