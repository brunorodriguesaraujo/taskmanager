package br.com.brunorodrigues.taskmanager.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.brunorodrigues.taskmanager.data.mapper.toNavModel
import br.com.brunorodrigues.taskmanager.databinding.FragmentTaskListBinding
import br.com.brunorodrigues.taskmanager.domain.model.Task
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskListViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedStateHandle()
        setupView()
        observeUiState()
        viewModel.getTasks()
    }

    private fun savedStateHandle() {
        val savedStateHandle = findNavController().currentBackStackEntry?.savedStateHandle
        savedStateHandle?.getLiveData<Boolean>("shouldRefresh")
            ?.observe(viewLifecycleOwner) { shouldRefresh ->
                if (shouldRefresh == true) {
                    viewModel.getTasks()
                    savedStateHandle.set("shouldRefresh", false)
                }
            }

    }

    private fun setupView() {
        adapter = TaskAdapter(requireContext()) {
            findNavController().navigate(
                TaskListFragmentDirections.actionTaskListFragmentToDetailTaskFragment(it.toNavModel())
            )
        }

        binding.run {
            recyclerTasks.adapter = adapter
            floatingAddTask.setOnClickListener {
                findNavController().navigate(
                    TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment()
                )
            }
        }

    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is TasksListUiState.Loading -> showLoading()
                    is TasksListUiState.Success -> {
                        if (state.tasks.isEmpty()) stateEmpty() else stateSuccess(state.tasks)
                    }

                    is TasksListUiState.Error -> stateError(state.message)
                }
            }
        }
    }

    private fun stateEmpty() = with(binding) {
        txtEmpty.isVisible = true
        hideLoading()
    }

    private fun showLoading() = with(binding) {
        progressBar.isVisible = true
        recyclerTasks.isVisible = false
    }

    private fun hideLoading() = with(binding) {
        progressBar.isVisible = false
        recyclerTasks.isVisible = true
    }

    private fun stateSuccess(tasks: List<Task>) {
        adapter.submitList(tasks)
        hideLoading()
    }

    private fun stateError(message: String) = with(binding) {
        txtError.text = message
        txtError.isVisible = true
        hideLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}