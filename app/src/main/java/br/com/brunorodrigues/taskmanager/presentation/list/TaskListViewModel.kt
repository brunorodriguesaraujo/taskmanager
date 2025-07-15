package br.com.brunorodrigues.taskmanager.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunorodrigues.taskmanager.domain.model.Task
import br.com.brunorodrigues.taskmanager.domain.usecase.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TasksListUiState {
    data object Loading : TasksListUiState()
    data class Success(val tasks: List<Task>) : TasksListUiState()
    data class Error(val message: String) : TasksListUiState()
}

@HiltViewModel
class TaskListViewModel @Inject constructor(private val getTasksUseCase: GetTasksUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow<TasksListUiState>(TasksListUiState.Loading)
    val uiState: StateFlow<TasksListUiState> = _uiState

    fun getTasks() {
        viewModelScope.launch {
            val result = getTasksUseCase()
            result
                .onSuccess { _uiState.value = TasksListUiState.Success(it) }
                .onFailure { _uiState.value = TasksListUiState.Error("Erro ao carregar") }
        }
    }

}