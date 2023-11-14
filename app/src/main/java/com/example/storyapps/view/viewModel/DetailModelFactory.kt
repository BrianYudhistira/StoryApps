package com.example.storyapps.view.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.data.Repository.StoryRepository
import com.example.storyapps.di.Injection
import com.example.storyapps.view.detail.DetailViewModel

class DetailModelFactory(private val storyRepository: StoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var INSTANCE: DetailModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): DetailModelFactory {
            if (INSTANCE == null) {
                synchronized(DetailModelFactory::class.java) {
                    INSTANCE = DetailModelFactory(Injection.provideStoryRepo(context))
                }
            }
            return INSTANCE as DetailModelFactory
        }
    }
}