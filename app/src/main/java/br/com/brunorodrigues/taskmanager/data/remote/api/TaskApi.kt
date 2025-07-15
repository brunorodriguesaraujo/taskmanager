package br.com.brunorodrigues.taskmanager.data.remote.api

import br.com.brunorodrigues.taskmanager.data.remote.dto.TaskDto

interface TaskApi {
    suspend fun getTasks(): List<TaskDto>
    suspend fun addTask(task: TaskDto) : TaskDto
    suspend fun updateTask(id: Int, task: TaskDto) : TaskDto
    suspend fun deleteTask(id: Int)
}