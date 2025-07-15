package br.com.brunorodrigues.taskmanager.presentation.form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.brunorodrigues.taskmanager.R
import br.com.brunorodrigues.taskmanager.databinding.FragmentAddTaskBinding
import br.com.brunorodrigues.taskmanager.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddTaskFragment : Fragment() {

    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFields()
        observeUiState()
        setupListener()
    }

    private fun setupListener() {
        binding.btnSave.setOnClickListener {
            viewModel.addTask()
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is AddTaskUiState.Idle -> {}
                    is AddTaskUiState.Loading -> showLoading()
                    is AddTaskUiState.Success -> stateSuccess()
                    is AddTaskUiState.Error -> stateError(state.message)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isButtonEnabled.collectLatest { isEnabled ->
                binding.btnSave.isEnabled = isEnabled
            }
        }
    }

    private fun stateError(message: String) {
        showToast(requireContext(), message = message)
        hideLoading()
    }

    private fun stateSuccess() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("shouldRefresh", true)
        findNavController().popBackStack()
    }

    private fun showLoading() = with(binding) {
        btnSave.text = ""
        loadingButton.isVisible = true
    }

    private fun hideLoading() = with(binding) {
        btnSave.text = getString(R.string.save)
        loadingButton.isVisible = false
    }

    private fun setupFields() = with(binding) {
        txtInputTitle.addTextChangedListener { viewModel.onTitleChanged(it.toString()) }
        txtInputDescription.addTextChangedListener { viewModel.onDescriptionChanged(it.toString()) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}