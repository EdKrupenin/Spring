package com.example.demo.service.impl

import com.example.demo.model.Task
import com.example.demo.repository.TaskRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class TaskServiceImplTest {

    @InjectMocks
    lateinit var taskService: TaskServiceImpl

    @Mock
    lateinit var taskRepository: TaskRepository

    private val date: LocalDate = LocalDate.now()

    @Test
    fun `test createTask`() = runTest {
        val task = Task(id = 1L, description = "Test Task", done = false, userId = 0, date = date)
        val userId = 1L
        val expectedTask = task.copy(userId = userId)

        `when`(taskRepository.save(any(Task::class.java))).thenReturn(expectedTask)

        taskService.createTask(task, userId)

        val captor = ArgumentCaptor.forClass(Task::class.java)
        verify(taskRepository).save(captor.capture())
        val savedTask = captor.value

        assertEquals(expectedTask, savedTask)
        assertEquals(userId, savedTask.userId)
    }

    @Test
    fun `test getTasksForUser`() = runTest {
        val userId = 1L
        val tasks = listOf(Task(id = 1L, description = "Test Task 1", done = false, userId = userId, date = date),
            Task(id = 2L, description = "Test Task 2", done = false, userId = userId, date = date))

        `when`(taskRepository.findAllByUserId(userId)).thenReturn(tasks)

        val result = taskService.getTasksForUser(userId)

        assertEquals(tasks, result)
    }

    @Test
    fun `test findAllTasks`() = runTest {
        val tasks = listOf(Task(id = 1L, description = "Test Task 1", done = false, userId = 1L, date = date),
            Task(id = 2L, description = "Test Task 2", done = false, userId = 1L, date = date))

        `when`(taskRepository.findAll()).thenReturn(tasks)

        val result = taskService.findAllTasks()

        assertEquals(tasks, result)
    }

    @Test
    fun `test findTaskById`() = runTest {
        val taskId = 1L
        val task = Task(id = taskId, description = "Test Task", done = false, userId = 1L, date = date)

        `when`(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(task))

        val result = taskService.findTaskById(taskId)

        assertEquals(task, result)
    }

    @Test
    fun `test updateTask`() = runTest {
        val taskId = 1L
        val task = Task(id = taskId, description = "Test Task", done = false, userId = 1L, date = date)

        `when`(taskRepository.save(any(Task::class.java))).thenReturn(task)

        taskService.updateTask(taskId, task)

        val captor = ArgumentCaptor.forClass(Task::class.java)
        verify(taskRepository).save(captor.capture())
        val updatedTask = captor.value

        assertEquals(task, updatedTask)
        assertEquals(taskId, updatedTask.id)
    }

    @Test
    fun `test deleteTask`() = runTest {
        val taskId = 1L

        taskService.deleteTask(taskId)

        verify(taskRepository).deleteById(taskId)
    }

    @Test
    fun `test markTaskAsDone`() = runTest {
        val taskId = 1L

        taskService.markTaskAsDone(taskId)

        verify(taskRepository).markDone(taskId)
    }

    @Test
    fun `test patchTask`() = runTest {
        val taskId = 1L
        val task = Task(id = taskId, description = "Test Task", done = true, userId = 1L, date = date)

        taskService.patchTask(taskId, task)

        verify(taskRepository).markDone(taskId)
    }
}
