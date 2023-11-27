package com.example.storyapps.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapps.api.ListStoryItem
import com.example.storyapps.databinding.ListItemBinding


class MainAdapter :
    PagingDataAdapter<ListStoryItem, MainAdapter.StoryViewHolder>(DiffCallback) {

    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val currentStory = getItem(position)
        if (currentStory != null) {
            holder.bind(currentStory)
        }
    }

    fun setStoryClickListener(listener: OnItemClickCallback) {
        onItemClickCallback = listener
    }

    inner class StoryViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    getItem(position)?.let { it1 -> onItemClickCallback?.onStoryClick(it1) }
                }
            }
        }

        fun bind(story: ListStoryItem) {
            binding.tvName.text = story.name
            binding.tvDesc.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .into(binding.ivImage)
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(
                oldItem: ListStoryItem, newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem, newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
    interface OnItemClickCallback {
        fun onStoryClick(story: ListStoryItem)
    }
