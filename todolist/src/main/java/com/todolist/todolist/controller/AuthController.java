package com.todolist.todolist.controller;

import com.todolist.todolist.dto.LoginDto;
import com.todolist.todolist.dto.SignUpDto;
import com.todolist.todolist.models.Role;
import com.todolist.todolist.models.User;
import com.todolist.todolist.repository.RoleRepository;
import com.todolist.todolist.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

//Register & Login
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private RoleRepository roleRepository ;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Create new account
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto)
    {
        //Check username and password
        if(userRepository.existsByUsername(signUpDto.getUsername()))
        {
            return  new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpDto.getEmail()))
        {
            return  new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        //add new data user
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());

        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role role = roleRepository.findByName(signUpDto.getRole()).get();
        user.setRoles(Collections.singleton(role));

        //save new user on database
        userRepository.save(user);

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    //User Login
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto)
    {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(),loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return  new ResponseEntity<>("Ok",HttpStatus.OK);

    }

    //ทีนี้ก่อนที่เราจะทำการสรา้ง Rout เราต้องไปทำการสร้างตัว security config ก่อน
    // เพื่อที่ว่ามันจะทำให้เราสามารถ สรา้งตัว Authentification หรือระบบยืนยันตัวตนได้
    //ก็คือตัว Spring security ที่เรา import dependency มาเเต่เเรก

}
