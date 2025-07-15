package br.com.brunorodrigues.taskmanager.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunorodrigues.taskmanager.domain.model.Task
import br.com.brunorodrigues.taskmanager.domain.usecase.DeleteTaskUseCase
import br.com.brunorodrigues.taskmanager.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UpdateTaskUiState {
    data object Idle : UpdateTaskUiState()
    data object Loading : UpdateTaskUiState()
    data object Success : UpdateTaskUiState()
    data class Error(val message: String) : UpdateTaskUiState()
}

@HiltViewModel
class UpdateTaskViewModel @Inject constructor(
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) :
    ViewModel() {

    private var _uiState = MutableStateFlow<UpdateTaskUiState>(UpdateTaskUiState.Idle)
    val uiState: StateFlow<UpdateTaskUiState> = _uiState

    private val title = MutableStateFlow("")
    private val description = MutableStateFlow("")

    val isButtonEnabled: StateFlow<Boolean> = combine(title, description) { title, desc ->
        title.isNotBlank() && desc.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun onTitleChanged(new: String) {
        title.value = new
    }

    fun onDescriptionChanged(new: String) {
        description.value = new
    }

    fun updateTask(task: Task) {
        _uiState.value = UpdateTaskUiState.Loading
        viewModelScope.launch {
            updateTaskUseCase(task)
        }.runCatching {
            when {
                isCompleted -> _uiState.value = UpdateTaskUiState.Success
                isCancelled -> _uiState.value = UpdateTaskUiState.Error("Não foi possível salvar")
            }
        }
    }

    fun deleteTask(id: Int) {
        _uiState.value = UpdateTaskUiState.Loading
        viewModelScope.launch { deleteTaskUseCase(id) }.runCatching {
            when {
                isCompleted -> _uiState.value = UpdateTaskUiState.Success
                isCancelled -> _uiState.value = UpdateTaskUiState.Error("Não foi possível salvar")
            }
        }
    }


}