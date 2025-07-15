package br.com.brunorodrigues.taskmanager.data.remote.api

import br.com.brunorodrigues.taskmanager.data.remote.dto.TaskDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TaskApiImpl(private val client: HttpClient, private val baseUrl: String) : TaskApi {
    override suspend fun getTasks(): List<TaskDto> {
        return client.get("$baseUrl/tasks").body()
    }

    override suspend fun addTask(task: TaskDto): TaskDto {
        val response = client.post("$baseUrl/tasks") {
            contentType(ContentType.Application.Json)
            setBody(task)
        }
        return response.body()
    }

    override suspend fun updateTask(id: Int, task: TaskDto): TaskDto {
        val response = client.post("$baseUrl/tasks/$id") {
            contentType(ContentType.Application.Json)
            setBody(task)
        }
        return response.body()
    }

    override suspend fun deleteTask(id: Int) {
        client.delete("$baseUrl/tasks/$id")
    }
}