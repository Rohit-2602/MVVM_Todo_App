package com.example.mvvmtodoapp.ui.tasks


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmtodoapp.data.PreferenceManager
import com.example.mvvmtodoapp.data.SortOrder
import com.example.mvvmtodoapp.data.Task
import com.example.mvvmtodoapp.data.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(private val taskDao: TaskDao, private val preferenceManager: PreferenceManager) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val preferencesFlow = preferenceManager.preferencesFlow

    private val taskFlow = combine(searchQuery, preferencesFlow) {
            query, filterPreferences -> Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    val getAllTasks = taskFlow.asLiveData()

    fun insert(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.insert(task)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        taskDao.deleteAll()
    }

    fun updateTask(oldTask: Task, newTask: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.update(oldTask.copy(name = newTask.name, important = newTask.important, completed = newTask.completed, created = newTask.created))
    }

    fun updateChecked(task: Task, isChecked : Boolean) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.update(task.copy(completed = isChecked))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.delete(task)
    }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch(Dispatchers.IO) {
        preferenceManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClicked(hideCompleted : Boolean) = viewModelScope.launch(Dispatchers.IO) {
        preferenceManager.updateHideCompleted(hideCompleted)
    }

}