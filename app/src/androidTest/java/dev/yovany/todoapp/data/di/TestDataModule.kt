package dev.yovany.todoapp.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.yovany.todoapp.data.RoomTaskLocalDataSource
import dev.yovany.todoapp.data.TaskDao
import dev.yovany.todoapp.data.TodoDatabase
import dev.yovany.todoapp.domain.TaskLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DataModule::class])
class TestDataModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase {
        return Room.inMemoryDatabaseBuilder(
            context.applicationContext,
            TodoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: TodoDatabase): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun provideTaskLocalDataSource(
        taskDao: TaskDao,
        dispatcher: CoroutineDispatcher): TaskLocalDataSource = RoomTaskLocalDataSource(taskDao, dispatcher )
}