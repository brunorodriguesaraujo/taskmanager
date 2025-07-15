package br.com.brunorodrigues.taskmanager.domain.repository

import br.com.brunorodrigues.taskmanager.domain.model.Task

interface TaskRepository {
    suspend fun getTasks(): List<Task>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(id: Int)
}