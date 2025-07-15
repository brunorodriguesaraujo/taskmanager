package br.com.brunorodrigues.taskmanager.domain.usecase

import br.com.brunorodrigues.taskmanager.data.repository.FakeTaskRepository
import br.com.brunorodrigues.taskmanager.domain.model.Task
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UpdateTaskUseCaseTest {

    @Test
    fun `updateTaskUseCase should modify an existing task`() = runTest {
        val initialTask = Task(1, "Initial", "Desc", false)
        val repository = FakeTaskRepository(listOf(initialTask))
        val useCase = UpdateTaskUseCase(repository)

        val updatedTask = initialTask.copy(title = "Updated", isDone = true)
        useCase(updatedTask)

        val task = repository.getTasks().find { it.id == 1 }
        assertEquals("Updated", task?.title)
        assertTrue(task?.isDone == true)
    }
}