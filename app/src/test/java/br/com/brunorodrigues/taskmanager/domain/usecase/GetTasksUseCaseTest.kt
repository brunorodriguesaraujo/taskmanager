package br.com.brunorodrigues.taskmanager.domain.usecase

import br.com.brunorodrigues.taskmanager.data.repository.FakeTaskRepository
import br.com.brunorodrigues.taskmanager.domain.model.Task
import br.com.brunorodrigues.taskmanager.domain.repository.TaskRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetTasksUseCaseTest {

    @Test
    fun `when repository return tasks, then useCase should return success`() = runTest {
        val fakeRepo = FakeTaskRepository(
            tasks = listOf(Task(1, "Title", "Desc", false))
        )
        val useCase = GetTasksUseCase(fakeRepo)

        val result = useCase()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `when getTasks is call, then should return error`() = runTest {
        val repository = object : TaskRepository {
            override suspend fun getTasks(): List<Task> = throw RuntimeException("Erro forçado")
            override suspend fun addTask(task: Task) {}
            override suspend fun updateTask(task: Task) {}
            override suspend fun deleteTask(id: Int) {}
        }

        val useCase = GetTasksUseCase(repository)
        val result = useCase()

        assertTrue(result.isFailure)
    }
}