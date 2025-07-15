package br.com.brunorodrigues.taskmanager.data.repository

import br.com.brunorodrigues.taskmanager.domain.model.Task
import br.com.brunorodrigues.taskmanager.domain.repository.TaskRepository
import kotlinx.coroutines.delay

class TaskRepositoryImpl : TaskRepository {

    private val tasks = mutableListOf(
        Task(1, "Estudar Android", "Estudar Arquitetura MVVM", false)
    )

    override suspend fun getTasks(): List<Task> {
        delay(1000)
        return tasks
    }

    override suspend fun addTask(task: Task) {
        val nextId = (tasks.maxOfOrNull { it.id ?: 0} ?: 0) + 1
        tasks.add(task.copy(id = nextId))
    }

    override suspend fun updateTask(task: Task) {
        tasks.replaceAll { if (it.id == task.id) task else it }
    }

    override suspend fun deleteTask(id: Int) {
        tasks.removeIf { it.id == id }
    }
}