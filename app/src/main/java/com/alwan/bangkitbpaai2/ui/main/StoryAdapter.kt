package com.alwan.bangkitbpaai2.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alwan.bangkitbpaai2.data.model.Story
import com.alwan.bangkitbpaai2.databinding.ItemStoryBinding
import com.alwan.bangkitbpaai2.util.loadImage

class StoryAdapter(private val callback: StoryCallback) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private val mData = ArrayList<Story>()

    fun setData(stories: ArrayList<Story>) {
        mData.clear()
        mData.addAll(stories)
        notifyDataSetChanged()
    }

    interface StoryCallback{
        fun onStoryClick(story: Story, itemBinding: ItemStoryBinding)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StoryViewHolder(
        ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) =
        holder.bind(mData[position])

    override fun getItemCount() = mData.size

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            with(binding) {
                imgStoryItem.loadImage(story.photoUrl)
                tvStoryItem.text = story.name
                root.setOnClickListener{ callback.onStoryClick(story, this) }
            }
        }
    }
}