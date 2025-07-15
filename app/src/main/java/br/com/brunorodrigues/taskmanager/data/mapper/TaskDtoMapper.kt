package br.com.brunorodrigues.taskmanager.data.mapper

import br.com.brunorodrigues.taskmanager.data.remote.dto.TaskDto
import br.com.brunorodrigues.taskmanager.domain.model.Task

fun Task.toDto() = TaskDto(id!!, title, description, isDone)

fun TaskDto.toDomain() = Task(id, title, description, isDone)