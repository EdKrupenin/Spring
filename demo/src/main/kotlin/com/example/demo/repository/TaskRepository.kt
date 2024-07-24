package com.example.demo.repository

import com.example.demo.model.Task
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface TaskRepository : PagingAndSortingRepository<Task, Long>, CrudRepository<Task, Long> {

    @Modifying
    @Query("UPDATE Task task SET task.done = true WHERE task.id = :id")
    fun markDone(@Param("id") id: Long)

    fun findAllByUserId(userId: Long): List<Task>
}