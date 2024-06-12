package com.todolist.todolist.controller;

import com.todolist.todolist.dto.TaskDto;
import com.todolist.todolist.models.Task;
import com.todolist.todolist.service.TaskService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    //Get all task / Get task by name
    @GetMapping("/")
    public ResponseEntity<?> getAllTask(Authentication authentication , @RequestParam(value = "name",defaultValue = "") String name) {
        String usernameOrEmail = authentication.getName();
        if (name.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTask(usernameOrEmail));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.OK).body(taskService.findTaskByName(usernameOrEmail,name));
        }
    }

    //Get task by id
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Task>> getTaskDetail(Authentication authentication, @PathVariable Long id){
        String usernameOrEmail = authentication.getName();
        Optional<Task> task =taskService.findTaskById(id,usernameOrEmail);
        return  ResponseEntity.status(HttpStatus.OK).body(task);
    }

    //Get all complete task
    @GetMapping("/completed")
    public ResponseEntity<?> getAllTaskCompleted(Authentication authentication)
    {
        String usernameOrEmail = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findAllCompletedTask(usernameOrEmail));
    }

    //Get all complete task
    @GetMapping("/uncompleted")
    public ResponseEntity<?> getAllTaskUnCompleted(Authentication authentication)
    {
        String usernameOrEmail = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body(taskService.findAllUnCompletedTask(usernameOrEmail));
    }

    //Update Task
    @PutMapping("/{id}")
    public ResponseEntity <? > updateTask(Authentication authentication, @PathVariable Long id ,@RequestBody Task task ){
        String usernameOrEmail = authentication.getName();
        Optional<Task> taskUpdated = taskService.updateTask(id, task, usernameOrEmail);

        if(taskUpdated.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found.");
        }
            return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);

    }

    //Create new task
    @PostMapping("/")
    public ResponseEntity<?> create(Authentication authentication , @Valid @RequestBody Task task)
    {
        TaskDto newtask = taskService.createTask(task,authentication.getName());
        return  ResponseEntity.status(HttpStatus.CREATED).body(newtask);
    }

    //Delete task by id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id,Authentication authentication)
    {
        String usernameOrEmail = authentication.getName();
        boolean isdelete = taskService.deleteTask(id,usernameOrEmail);

        if(isdelete)
        {
            return ResponseEntity.status(HttpStatus.OK).body("Task daleted");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Task not found");

    }
}
