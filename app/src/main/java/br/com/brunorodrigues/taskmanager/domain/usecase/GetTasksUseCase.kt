package br.com.brunorodrigues.taskmanager.domain.usecase

import br.com.brunorodrigues.taskmanager.domain.model.Task
import br.com.brunorodrigues.taskmanager.domain.repository.TaskRepository

class GetTasksUseCase (private val repository: TaskRepository) {
    suspend operator fun invoke(): Result<List<Task>> = runCatching { repository.getTasks() }
}