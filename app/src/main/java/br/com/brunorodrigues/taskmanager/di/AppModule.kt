package br.com.brunorodrigues.taskmanager.di

import br.com.brunorodrigues.taskmanager.data.repository.TaskRepositoryImpl
import br.com.brunorodrigues.taskmanager.domain.repository.TaskRepository
import br.com.brunorodrigues.taskmanager.domain.usecase.AddTaskUseCase
import br.com.brunorodrigues.taskmanager.domain.usecase.DeleteTaskUseCase
import br.com.brunorodrigues.taskmanager.domain.usecase.GetTasksUseCase
import br.com.brunorodrigues.taskmanager.domain.usecase.UpdateTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskRepository(): TaskRepository = TaskRepositoryImpl()

    @Provides
    @Singleton
    fun provideGetTasksUseCase(repository: TaskRepository): GetTasksUseCase =
        GetTasksUseCase(repository)

    @Provides
    @Singleton
    fun provideAddTaskUseCase(repository: TaskRepository): AddTaskUseCase =
        AddTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateTaskUseCase(repository: TaskRepository): UpdateTaskUseCase =
        UpdateTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteTaskUseCase(repository: TaskRepository): DeleteTaskUseCase =
        DeleteTaskUseCase(repository)
}