package br.com.brunorodrigues.taskmanager.presentation.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunorodrigues.taskmanager.domain.model.Task
import br.com.brunorodrigues.taskmanager.domain.usecase.AddTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AddTaskUiState {
    data object Idle: AddTaskUiState()
    data object Loading : AddTaskUiState()
    data object Success : AddTaskUiState()
    data class Error(val message: String) : AddTaskUiState()
}

@HiltViewModel
class AddTaskViewModel @Inject constructor(private val addTaskUseCase: AddTaskUseCase) :
    ViewModel() {

    private var _uiState = MutableStateFlow<AddTaskUiState>(AddTaskUiState.Idle)
    val uiState: StateFlow<AddTaskUiState> = _uiState

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

    fun addTask() {
        _uiState.value = AddTaskUiState.Loading
        viewModelScope.launch {
            addTaskUseCase(
                Task(
                    title = title.value,
                    description = description.value,
                    isDone = false
                )
            )
        }.runCatching {
            when {
                isCompleted -> _uiState.value = AddTaskUiState.Success
                isCancelled -> _uiState.value = AddTaskUiState.Error("Não foi possível salvar")
            }
        }
    }


}