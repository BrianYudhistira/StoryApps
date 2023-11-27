package com.example.storyapps.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.storyapps.databinding.ActivityDetailBinding
import com.example.storyapps.ui.viewModel.DetailModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        DetailModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)

        val token = intent.getStringExtra("token").toString()
        val id = intent.getStringExtra("id").toString()

        Log.d("token", token)
        Log.d("id", id)

        viewModel.getStoryDetail(token, id)

        viewModel.detailStory.observe(this) {
            binding.tvUsername.text = it.name
            binding.tvStory.text = it.description

            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.image)
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

