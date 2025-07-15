package br.com.brunorodrigues.taskmanager.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TaskDto(
    val id: Int,
    val title: String,
    val description: String,
    val isDone: Boolean
)