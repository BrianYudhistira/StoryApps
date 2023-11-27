package com.example.storyapps.ui.detail

import com.example.storyapps.data.repository.StoryRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapps.api.ListStoryItem
import com.example.storyapps.api.Story
import kotlinx.coroutines.launch


class DetailViewModel(private val story: StoryRepository) : ViewModel() {

    private val _detailStory = MutableLiveData<Story>()

    val detailStory: LiveData<Story> get() = _detailStory

    fun getStoryDetail(token: String, id: String) {
        viewModelScope.launch {

            try {
                val detailStory = story.getDetail(token, id)
                _detailStory.value = detailStory

            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    fun getAllStory(token: String) : LiveData<PagingData<ListStoryItem>> {
        return story.getStories(token).cachedIn(viewModelScope) }
}
