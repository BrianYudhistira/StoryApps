package com.example.storyapps.data.Repository

import com.example.storyapps.api.ListStoryItem
import com.example.storyapps.api.Story
import com.example.storyapps.api.config.ApiConfig
import com.example.storyapps.api.config.ApiService

class StoryRepository(private val apiService: ApiService) {
    suspend fun getStories(token: String, onSuccess: (List<ListStoryItem>) -> Unit, onError: (String) -> Unit) {
        try {
            val response = ApiConfig.getApiService(token).getAllStory()
            if (response.error) {
                onError(response.message)
            } else {
                onSuccess(response.listStory)
            }
        } catch (e: Exception) {
            onError(e.message ?: "Unknown error")
        }
    }
    suspend fun getDetail(token:String, id: String): Story {
        val response = ApiConfig.getApiService(token).getDetailStory(id)
        if (response.error) {
            throw Exception(response.message)
        }
        return response.story
    }
    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService).also { instance = it }
            }
    }
}