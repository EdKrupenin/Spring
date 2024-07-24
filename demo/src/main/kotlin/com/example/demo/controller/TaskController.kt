package com.example.demo.controller

import com.example.demo.model.Task
import com.example.demo.security.CustomUserDetails
import com.example.demo.service.TaskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
class TaskController @Autowired constructor(val taskService: TaskService) {

    private fun getCurrentUserId(): Long {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as CustomUserDetails
        return userDetails.id
    }

    @PostMapping("/tasks")
    suspend fun createTask(@RequestBody task: Task): Task {
        val userId = getCurrentUserId()
        return taskService.createTask(task, userId)
    }

    @GetMapping("/tasks")
    suspend fun findAll(): List<Task> {
        val userId = getCurrentUserId()
        return taskService.getTasksForUser(userId)
    }

    @GetMapping("/tasks/{id}")
    suspend fun findById(@PathVariable("id") id: Long): Task {
        return taskService.findTaskById(id)
    }

    @PutMapping("/tasks/{id}")
    suspend fun updateTask(@PathVariable("id") id: Long, @RequestBody task: Task): Task {
        return taskService.updateTask(id, task)
    }

    @DeleteMapping("/tasks/{id}")
    suspend fun deleteTask(@PathVariable("id") id: Long) {
        taskService.deleteTask(id)
    }

    @PatchMapping("/tasks/{id}:mark-as-done")
    suspend fun patchMethod(@PathVariable("id") id: Long) {
        taskService.markTaskAsDone(id)
    }

    @PatchMapping("/tasks/{id}")
    suspend fun patchMethod(@PathVariable("id") id: Long, @RequestBody task: Task) {
        taskService.patchTask(id, task)
    }
}