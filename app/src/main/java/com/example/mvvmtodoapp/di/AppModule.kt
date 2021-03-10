package com.example.mvvmtodoapp.di

import android.app.Application
import androidx.room.Room
import com.example.mvvmtodoapp.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app : Application, callback : TaskDatabase.CallBack) =
        Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    @Provides
    fun provideDao(database: TaskDatabase) = database.taskDao()

//    @Provides
//    @Singleton
//    @ApplicationScope
//    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}

//@Retention(AnnotationRetention.RUNTIME)
//@Qualifier
//annotation class ApplicationScope