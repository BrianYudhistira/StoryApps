package com.example.storyapps.view.detail

import StoryRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}
