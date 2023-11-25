package com.example.storyapps.di

import StoryRepository
import android.content.Context
import com.example.storyapps.api.config.ApiConfig
import com.example.storyapps.data.Repository.UserRepository
import com.example.storyapps.data.pref.UserPreference
import com.example.storyapps.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
    fun provideStoryRepo(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository()
    }
}