package br.com.brunorodrigues.taskmanager.domain.usecase

import br.com.brunorodrigues.taskmanager.data.repository.FakeTaskRepository
import br.com.brunorodrigues.taskmanager.domain.model.Task
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Test

class AddTaskUseCaseTest {

    @Test
    fun `addTaskUseCase should add task with generated ID`() = runTest {
        val repository = FakeTaskRepository()
        val useCase = AddTaskUseCase(repository)

        val task = Task(title = "New Task", description = "Details", isDone = false)

        useCase(task)

        val added = repository.getTasks().firstOrNull()
        assertNotNull(added)
        assertEquals("New Task", added?.title)
        assertNotNull(added?.id)
    }
}