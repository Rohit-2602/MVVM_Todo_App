package com.example.mvvmtodoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM task_table WHERE name LIKE '%' || :searchQuery || '%' ORDER BY important DESC")
    fun getTasks(searchQuery : String, sortOrder: SortOrder, hideCompleted : Boolean) : Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_NAME -> getTaskByName(searchQuery, hideCompleted)
            SortOrder.BY_DATE -> getTaskByDate(searchQuery, hideCompleted)
        }

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, name")
    fun getTaskByName(searchQuery: String, hideCompleted: Boolean) : Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, created")
    fun getTaskByDate(searchQuery: String, hideCompleted: Boolean) : Flow<List<Task>>

}