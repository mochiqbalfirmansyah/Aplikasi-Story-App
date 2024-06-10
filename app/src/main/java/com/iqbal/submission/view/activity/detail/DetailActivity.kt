package com.iqbal.submission.view.activity.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.iqbal.submission.R
import com.iqbal.submission.data.api.response.ListStoryItem
import com.iqbal.submission.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.detail_story)

        val story = intent.getParcelableExtra<ListStoryItem>("STORY")

        if (story != null) {
            with(binding){
                Glide.with(this@DetailActivity)
                    .load(story.photoUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.baseline_restart_alt_24).error(R.drawable.baseline_broken_image_24))
                    .into(ivDetailPhoto)
                tvDetailName.text = story.name
                tvDetailDescription.text = story.description
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}