package br.com.brunorodrigues.taskmanager.domain.usecase

import br.com.brunorodrigues.taskmanager.data.repository.FakeTaskRepository
import br.com.brunorodrigues.taskmanager.domain.model.Task
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteTaskUseCaseTest {

    @Test
    fun `deleteTaskUseCase should remove task by id`() = runTest {
        val task = Task(10, "Delete me", "Soon", false)
        val repository = FakeTaskRepository(listOf(task))
        val useCase = DeleteTaskUseCase(repository)

        useCase(task.id!!)

        assertTrue(repository.getTasks().none { it.id == task.id })
    }
}