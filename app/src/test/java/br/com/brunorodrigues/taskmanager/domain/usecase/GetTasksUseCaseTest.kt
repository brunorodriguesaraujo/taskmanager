package br.com.brunorodrigues.taskmanager.domain.usecase

import br.com.brunorodrigues.taskmanager.data.repository.FakeTaskRepository
import br.com.brunorodrigues.taskmanager.domain.model.Task
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetTasksUseCaseTest {

    @Test
    fun `when repository return tasks, useCase should return success`() = runTest {
        val fakeRepo = FakeTaskRepository(
            tasks = listOf(Task(1, "Title", "Desc", false))
        )
        val useCase = GetTasksUseCase(fakeRepo)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
    }
}