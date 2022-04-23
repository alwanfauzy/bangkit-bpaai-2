package com.alwan.bangkitbpaai2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.alwan.bangkitbpaai2.R
import com.alwan.bangkitbpaai2.data.model.Story
import com.alwan.bangkitbpaai2.databinding.ActivityDetailBinding
import com.alwan.bangkitbpaai2.util.loadImage

class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private var imageScaleZoom = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.detail_story)

        val story = intent.getParcelableExtra<Story>(EXTRA_STORY)
        setupView(story)
    }

    private fun setupView(story: Story?) {
        with(binding) {
            imgStoryDetail.loadImage(story?.photoUrl)
            tvTitleDetail.text = story?.name
            tvDescDetail.text = story?.description

            imgStoryDetail.setOnClickListener(this@DetailActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.imgStoryDetail -> {
                imageScaleZoom = !imageScaleZoom
                binding.imgStoryDetail.scaleType =
                    if (imageScaleZoom) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
            }
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}