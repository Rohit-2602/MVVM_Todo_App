package com.example.mvvmtodoapp.ui.tasks

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmtodoapp.data.Task
import com.example.mvvmtodoapp.databinding.ItemTaskBinding

class TaskAdapter(private val listener : OnItemClickListener) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                checkbox.setOnClickListener {
                    val currentTask = getItem(adapterPosition)
                    listener.onCheckBoxClicked(currentTask, checkbox.isChecked)
                }
                taskNameTextView.setOnClickListener {
                    val currentTask = getItem(adapterPosition)
                    listener.onItemClicked(currentTask)
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                checkbox.isChecked = task.completed
                taskNameTextView.text = task.name
                taskNameTextView.paint.isStrikeThruText = task.completed
                if (task.important) {
                    taskNameTextView.setTextColor(Color.parseColor("#FF03DAC5"))
                }
                dateCreatedTextView.text = task.createdDateFormatted
                timeCreatedTextView.text = task.createdTimeFormatted
            }
        }

    }

    class DiffCallBack : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onCheckBoxClicked(task: Task, isChecked : Boolean)
        fun onItemClicked(task: Task)
    }

}