package br.com.brunorodrigues.taskmanager.presentation.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskNavModel(
    val id: Int? = null,
    val title: String,
    val description: String,
    val isDone: Boolean
): Parcelable