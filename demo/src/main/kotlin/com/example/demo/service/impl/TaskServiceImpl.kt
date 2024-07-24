package com.example.demo.service.impl

import com.example.demo.model.Task
import com.example.demo.repository.TaskRepository
import com.example.demo.service.TaskService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TaskServiceImpl @Autowired constructor(
    private val taskRepository: TaskRepository,
) : TaskService {
    override suspend fun createTask(task: Task, userId: Long): Task = withContext(Dispatchers.IO) {
        taskRepository.save(task.copy(userId = userId))
    }

    override suspend fun getTasksForUser(userId: Long): List<Task> = withContext(Dispatchers.IO) {
        taskRepository.findAllByUserId(userId).toMutableList()
    }

    override suspend fun findAllTasks(): List<Task> = withContext(Dispatchers.IO) {
        taskRepository.findAll().toMutableList()
    }

    override suspend fun findTaskById(id: Long): Task = withContext(Dispatchers.IO) {
        taskRepository.findById(id).orElseThrow { RuntimeException("Task not found") }
    }

    override suspend fun updateTask(id: Long, task: Task): Task = withContext(Dispatchers.IO) {
        taskRepository.save(task.copy(id = id))
    }

    override suspend fun deleteTask(id: Long) = withContext(Dispatchers.IO) {
        taskRepository.deleteById(id)
    }

    override suspend fun markTaskAsDone(id: Long) = withContext(Dispatchers.IO) {
        taskRepository.markDone(id)
    }

    override suspend fun patchTask(id: Long, task: Task): Unit = withContext(Dispatchers.IO) {
        task.takeIf { it.done }?.let { taskRepository.markDone(id) }
    }
}