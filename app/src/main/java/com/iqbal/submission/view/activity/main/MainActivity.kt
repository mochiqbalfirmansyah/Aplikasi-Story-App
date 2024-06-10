package com.iqbal.submission.view.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iqbal.submission.R
import com.iqbal.submission.data.api.response.ListStoryItem
import com.iqbal.submission.databinding.ActivityMainBinding
import com.iqbal.submission.view.activity.ViewModelFactory
import com.iqbal.submission.view.activity.login.LoginActivity
import com.iqbal.submission.view.adapter.StoryAdapter
import com.iqbal.submission.utils.Result
import com.iqbal.submission.view.activity.addStory.AddStoryActivity
import com.iqbal.submission.view.activity.detail.DetailActivity
import com.iqbal.submission.view.activity.maps.MapsActivity
import com.iqbal.submission.view.adapter.LoadingStateAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupRecyclerView()
        setupAddStory()

        viewModel.getSession().observe(this){ user->
            if (user.isLogin){
                getAllStories()
            } else {
                navigateToLogin()
            }
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.btn_logout -> {
                    viewModel.logout()
                    navigateToLogin()
                    true
                }
                R.id.maps -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToLogin(){
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter()
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun setupAddStory() {
        binding.btnAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllStories() {
        val adapter = StoryAdapter()
        with(binding){
            rvStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )

        }
        viewModel.story.observe(this@MainActivity) {
            binding.progressBar.visibility = View.INVISIBLE
            adapter.submitData(lifecycle, it)
            adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListStoryItem) {
                    val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
                    intentToDetail.putExtra("STORY", data)
                    startActivity(intentToDetail)
                }
            })
        }

    }
}