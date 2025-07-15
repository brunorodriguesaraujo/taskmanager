package br.com.brunorodrigues.taskmanager.domain.usecase

import br.com.brunorodrigues.taskmanager.domain.model.Task
import br.com.brunorodrigues.taskmanager.domain.repository.TaskRepository

class AddTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(task: Task) {
        return repository.addTask(task)
    }
}