package ru.prod.application.mainMenu.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ru.prod.application.auth.AuthManager
import ru.prod.application.mainMenu.data.source.network.GroupInfoApi
import ru.prod.application.mainMenu.data.source.network.MyDebtsApi
import ru.prod.application.mainMenu.data.source.network.MyGroupsApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainMenuModule {
    @Provides
    @Singleton
    fun provideMyDebtsApi(authManager: AuthManager, httpClient: HttpClient): MyDebtsApi = MyDebtsApi(authManager, httpClient)
    @Provides
    @Singleton
    fun provideMyGroupsApi(httpClient: HttpClient, authManager: AuthManager): MyGroupsApi = MyGroupsApi(httpClient, authManager)
    @Provides
    @Singleton
    fun provideGroupInfoApi(): GroupInfoApi = GroupInfoApi()
}