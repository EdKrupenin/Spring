package com.example.demo.service

import com.example.demo.model.Task

interface TaskService {
    suspend fun createTask(task: Task, userId: Long): Task
    suspend fun getTasksForUser(userId: Long): List<Task>
    suspend fun findAllTasks(): List<Task>
    suspend fun findTaskById(id: Long): Task
    suspend fun updateTask(id: Long, task: Task): Task
    suspend fun deleteTask(id: Long)
    suspend fun markTaskAsDone(id: Long)
    suspend fun patchTask(id: Long, task: Task)
}