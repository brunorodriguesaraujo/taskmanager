package br.com.brunorodrigues.taskmanager.di

import br.com.brunorodrigues.taskmanager.data.remote.api.TaskApi
import br.com.brunorodrigues.taskmanager.data.remote.api.TaskApiImpl
import br.com.brunorodrigues.taskmanager.data.remote.core.KtorClientProvider
import br.com.brunorodrigues.taskmanager.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = KtorClientProvider.client

    @Provides
    @Singleton
    fun provideTaskApi(client: HttpClient): TaskApi =
        TaskApiImpl(client, baseUrl = BASE_URL)
}