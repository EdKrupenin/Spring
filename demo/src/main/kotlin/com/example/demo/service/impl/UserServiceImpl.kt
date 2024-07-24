package com.example.demo.service.impl

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import com.example.demo.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl @Autowired constructor(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserService {

    override suspend fun create(user: User) {
        withContext(Dispatchers.IO) {
            val encodedUser = user.copy(password = passwordEncoder.encode(user.password))
            userRepository.save(encodedUser)
        }
    }

    override suspend fun getCurrentUser(): User {
        val authentication: Authentication = SecurityContextHolder.getContext().authentication
        val principal =
            authentication.principal as org.springframework.security.core.userdetails.User

        return withContext(Dispatchers.IO) {
            userRepository.findByLogin(principal.username)
        } ?: throw UsernameNotFoundException("user not found")
    }
}