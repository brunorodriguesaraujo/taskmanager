package br.com.brunorodrigues.taskmanager.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.brunorodrigues.taskmanager.R
import br.com.brunorodrigues.taskmanager.data.mapper.toDomain
import br.com.brunorodrigues.taskmanager.databinding.FragmentUpdateTaskBinding
import br.com.brunorodrigues.taskmanager.utils.extension.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpdateTaskFragment : Fragment() {

    private var _binding: FragmentUpdateTaskBinding? = null
    private val binding get() = _binding!!
    private val args: UpdateTaskFragmentArgs by navArgs()
    private val task by lazy { args.task.toDomain() }
    private val viewModel: UpdateTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupListener()
        observeUiState()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is UpdateTaskUiState.Idle -> {}
                    is UpdateTaskUiState.Loading -> showLoading()
                    is UpdateTaskUiState.Success -> stateSuccess()
                    is UpdateTaskUiState.Error -> stateError(state.message)
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
        showToast(requireContext(), message)
    }

    private fun stateSuccess() {
        findNavController().previousBackStackEntry?.savedStateHandle?.set("shouldRefresh", true)
        findNavController().popBackStack()
    }

    private fun showLoading() {

    }

    private fun setupView() = with(binding) {
        txtInputTitle.setText(task.title)
        txtInputDescription.setText(task.description)
        viewModel.onTitleChanged(task.title)
        viewModel.onDescriptionChanged(task.description)
        if (task.isDone) {
            checkboxDone.isChecked = true
            setChecked()
        } else {
            checkboxDone.isChecked = false
            setUnchecked()
        }
            checkboxDone.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) setChecked() else setUnchecked()
            }
    }

    private fun setChecked() = with(binding) {
        checkboxDone.text = getString(R.string.task_done)
        checkboxDone.setTextColor(ContextCompat.getColor(requireContext(), R.color._4caf50))
    }

    private fun setUnchecked() = with(binding) {
        checkboxDone.text = getString(R.string.mark_completed)
        checkboxDone.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.black
            )
        )
    }

    private fun setupListener() = with(binding) {
        txtInputTitle.addTextChangedListener { viewModel.onTitleChanged(it.toString()) }
        txtInputDescription.addTextChangedListener { viewModel.onDescriptionChanged(it.toString()) }
        btnSave.setOnClickListener {
            viewModel.updateTask(
                task.copy(
                    title = txtInputTitle.text.toString(),
                    description = txtInputDescription.text.toString(),
                    isDone = checkboxDone.isChecked
                )
            )
        }
        btnDelete.setOnClickListener { viewModel.deleteTask(task.id!!) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}