package com.todolist.todolist.dto;

import lombok.Data;

//Format for response user when user Sign up
@Data
public class SignUpDto {
    private  String name;
    private  String username;
    private  String email;
    private  String password;
    private  String role;

}
