package com.todolist.todolist.dto;

import lombok.Data;
////Format for response user when user created new task
@Data
public class TaskDto {
    private Long owner_id;

    private String name;

    private String desc;

    private boolean complete;

}
