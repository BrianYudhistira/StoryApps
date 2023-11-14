package com.example.storyapps.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapps.R
import com.example.storyapps.view.viewModel.ViewModelFactory
import com.example.storyapps.api.ListStoryItem
import com.example.storyapps.api.config.ApiConfig
import com.example.storyapps.data.Repository.StoryRepository
import com.example.storyapps.databinding.ActivityMainBinding
import com.example.storyapps.view.detail.DetailActivity
import com.example.storyapps.view.upload.UploadActivity
import com.example.storyapps.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter
    var token: String = ""
    private val apiService = ApiConfig.getApiService(token)
    private val storyRepository = StoryRepository(apiService)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    val intent = Intent(this@MainActivity,UploadActivity::class.java)
                        intent.putExtra("token", token)
                    startActivity(intent)
                    true
                }

                R.id.logout -> {
                    viewModel.logout()
                    true
                }

                else -> false
            }
        }
        setupView()
        getStories()

        adapter.setStoryClickListener(object : OnItemClickCallback {
            override fun onStoryClick(story: ListStoryItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("id", story.id.toString())
                intent.putExtra("token", token)
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle())
            }
        })
    }

    private fun setupView() {
        adapter = MainAdapter()
        binding.recyclerView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun updateStoryList(storyList: List<ListStoryItem>) {
        adapter.submitList(storyList)
    }

    private fun getStories() {
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                token = user.token

                viewModel.viewModelScope.launch {
                    try {
                        storyRepository.getStories(
                            token!!,
                            onSuccess = { storyList ->
                                updateStoryList(storyList)
                            },
                            onError = {
                            }
                        )
                    } catch (e: Exception) {
                        Log.d("Error", e.message.toString())
                    }
                }
            }
        }
        showLoading(false)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}