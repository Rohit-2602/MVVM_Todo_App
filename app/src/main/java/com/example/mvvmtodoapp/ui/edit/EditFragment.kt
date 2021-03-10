package com.example.mvvmtodoapp.ui.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mvvmtodoapp.R
import com.example.mvvmtodoapp.data.Task
import com.example.mvvmtodoapp.databinding.FragmentAddTaskBinding
import com.example.mvvmtodoapp.ui.tasks.TasksViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditFragment : Fragment(R.layout.fragment_add_task) {

    private val viewModel : TasksViewModel by viewModels()
    private val navArgs : EditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddTaskBinding.bind(view)

        binding.apply {

            val task = navArgs.Tasks

            taskNameEditText.setText(navArgs.Tasks?.name)
            if (task != null) {
                importantCheckBox.isChecked = task.important
                dateCreatedTextView.text = task.createdDateTimeFormatted
            }

            saveFab.setOnClickListener {
                val taskName = taskNameEditText.text.toString()
                val newTask = Task(taskName, importantCheckBox.isChecked)

                if (taskName.isEmpty()) {
                    Snackbar.make(requireView(), "Name Can't Be Empty", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (task == null) {
                    viewModel.insert(newTask)
                }
                else{
                    viewModel.updateTask(task, newTask)
                }
                val action = EditFragmentDirections.actionEditFragmentToTaskFragment()
                findNavController().navigate(action)
            }
        }

    }

}