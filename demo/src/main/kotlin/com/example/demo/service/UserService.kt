package com.example.demo.service

import com.example.demo.model.User

interface UserService {
    suspend fun create(user: User)
    suspend fun getCurrentUser(): User
}