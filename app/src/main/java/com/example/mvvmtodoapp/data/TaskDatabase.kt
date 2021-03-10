package com.example.mvvmtodoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class CallBack @Inject constructor(
        private val database: Provider<TaskDatabase>
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            CoroutineScope(Dispatchers.IO).launch {
                dao.insert(Task("Task 1"))
                dao.insert(Task("Task 2"))
                dao.insert(Task("Task 3", completed = true))
                dao.insert(Task("Task 4", important = true))
                dao.insert(Task("Task 5"))
                dao.insert(Task("Task 6", important = true, completed = true))
            }

//            applicationScope.launch {
//                dao.insert(Task("Task 1"))
//                dao.insert(Task("Task 2"))
//                dao.insert(Task("Task 3", completed = true))
//                dao.insert(Task("Task 4", important = true))
//                dao.insert(Task("Task 5"))
//                dao.insert(Task("Task 6", important = true, completed = true))
//            }

        }

    }

}
