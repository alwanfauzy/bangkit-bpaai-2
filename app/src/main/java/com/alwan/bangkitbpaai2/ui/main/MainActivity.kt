package com.alwan.bangkitbpaai2.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.alwan.bangkitbpaai2.R
import com.alwan.bangkitbpaai2.data.Resource
import com.alwan.bangkitbpaai2.data.model.Story
import com.alwan.bangkitbpaai2.databinding.ActivityMainBinding
import com.alwan.bangkitbpaai2.databinding.ItemStoryBinding
import com.alwan.bangkitbpaai2.ui.addstory.AddStoryActivity
import com.alwan.bangkitbpaai2.ui.DetailActivity
import com.alwan.bangkitbpaai2.ui.SettingActivity
import com.alwan.bangkitbpaai2.util.GridMarginItemDecoration
import com.alwan.bangkitbpaai2.util.UserPreferences
import com.alwan.bangkitbpaai2.util.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), StoryAdapter.StoryCallback {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val storyAdapter = StoryAdapter(this)
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
    }

    private fun setupView() {
        setupRecyclerView()
    }

    private fun setupViewModel() {
        val pref = UserPreferences.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(pref)
        viewModelFactory.setApplication(application)

        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        mainViewModel.stories.observe(this) {
            when (it) {
                is Resource.Success -> {
                    it.data?.let { stories -> storyAdapter.setData(stories) }
                    showLoading(false)
                }
                is Resource.Loading -> showLoading(true)
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }

        fetchData()
    }

    private fun setupRecyclerView() {
        with(binding.rvStory) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, RV_COLUMN_COUNT)
            addItemDecoration(GridMarginItemDecoration(RV_COLUMN_COUNT, 16, true))
            adapter = storyAdapter
        }
    }

    private fun showLoading(state: Boolean) {
        binding.spinMain.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun fetchData() {
        CoroutineScope(Dispatchers.IO).launch {
            mainViewModel.getStories()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add_story -> {
                startActivity(Intent(this, AddStoryActivity::class.java))
                true
            }
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingActivity::class.java))
                true
            }
            else -> true
        }
    }

    override fun onStoryClick(story: Story, itemBinding: ItemStoryBinding) {
        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(itemBinding.imgStoryItem, "imageDetail"),
                Pair(itemBinding.tvStoryItem, "titleDetail"),
            )

        val detailIntent = Intent(this, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.EXTRA_STORY, story)
        startActivity(detailIntent, optionsCompat.toBundle())
    }

    companion object {
        const val RV_COLUMN_COUNT = 2
    }
}