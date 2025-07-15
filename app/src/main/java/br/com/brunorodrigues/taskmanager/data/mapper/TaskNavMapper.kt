package br.com.brunorodrigues.taskmanager.data.mapper

import br.com.brunorodrigues.taskmanager.domain.model.Task
import br.com.brunorodrigues.taskmanager.presentation.list.TaskNavModel

fun Task.toNavModel() = TaskNavModel(
    id = id,
    title = title,
    description = description,
    isDone = isDone
)

fun TaskNavModel.toDomain() = Task(
    id = id,
    title = title,
    description = description,
    isDone = isDone
)