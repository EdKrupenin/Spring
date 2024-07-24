package com.example.demo.controller

import com.example.demo.model.User
import com.example.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController @Autowired constructor(val userService: UserService) {
    @PostMapping("/registration")
    suspend fun registration(
        @RequestParam(value = "login", defaultValue = "") login: String,
        @RequestParam(value = "password", defaultValue = "") password: String
    ): ResponseEntity<String> {
        return if (login.isBlank() || password.isBlank()) {
            ResponseEntity("Login and password must not be empty", HttpStatus.BAD_REQUEST)
        } else {
            userService.create(User(login = login, password = password))
            ResponseEntity("User successfully registered", HttpStatus.CREATED)
        }
    }
}