package br.com.brunorodrigues.taskmanager.domain.usecase

import br.com.brunorodrigues.taskmanager.domain.repository.TaskRepository

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Int) {
        return repository.deleteTask(id)
    }
}