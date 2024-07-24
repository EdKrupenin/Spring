package com.example.demo.repository

import com.example.demo.model.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : PagingAndSortingRepository<User, Long>, CrudRepository<User, Long> {
    fun findByLogin(username: String): User?
}