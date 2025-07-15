package br.com.brunorodrigues.taskmanager.presentation.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import br.com.brunorodrigues.taskmanager.R
import br.com.brunorodrigues.taskmanager.databinding.ItemTaskBinding
import br.com.brunorodrigues.taskmanager.domain.model.Task

class TaskAdapter(
    private val context: Context,
    private val onClick: (Task) -> Unit,
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : ViewHolder(binding.root) {
        fun bind(task: Task) = with(binding) {
            txtTitle.text = task.title
            txtDescription.text = task.description
            if (task.isDone) setViewDoneTask() else setViewPendingTask()

            root.setOnClickListener {
                onClick(task)
            }
        }

        private fun setViewDoneTask() {
            binding.txtStatus.setTextColor(ContextCompat.getColor(context, R.color._4caf50))
            binding.txtStatus.text = context.getString(R.string.done)
        }

        private fun setViewPendingTask() {
            binding.txtStatus.setTextColor(ContextCompat.getColor(context, R.color._e91e1e))
            binding.txtStatus.text = context.getString(R.string.pending)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean =
            oldItem == newItem
    }
}