package com.example.githubproject.usif.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.githubproject.R
import com.example.githubproject.data.local.FavoriteUser
import com.example.githubproject.databinding.ActivityUserDetailBinding
import com.example.githubproject.usif.favorite.FavViewModel
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var Binding: ActivityUserDetailBinding
    private var avatarUrl: String? = null
    private val viewModel by viewModels<DetailUserViewModel>()
    private val favViewModel: FavViewModel by viewModels{ ViewModelFactory.getInstance(application) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        val username = intent.getStringExtra(EXTRA_NAME)
        val sectionPagerAdapter = SectionPagerAdapter(this)

        sectionPagerAdapter.username = username.toString()
        Binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(Binding.tab, Binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        if (username != null){
            viewModel.setUserDetail(username)
        }
        viewModel.getUserDetail().observe(this,{
            if (it != null){
                Binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = String.format("%d  Followers", it.followers)
                    tvFollowing.text = String.format("%d  Following", it.following)
                    Glide.with(this@UserDetailActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(ivProfile)
                }
                avatarUrl = it.avatarUrl
            }
        })

        viewModel.isLoad().observe(this){
            showLoading(it)
        }

        favViewModel.getFavorited()?.observe(this) { listFavorite ->

            val isFavorited = listFavorite.any {
                it.username == username
            }
            Binding.favToggle.setOnClickListener {
                Binding.favToggle.animate().apply {
                    duration = 500
                    rotationYBy(180F)
                }
                Binding.bgCirlce.animate().apply {
                    duration = 500
                    rotationYBy(180F)
                }
                val entity = username?.let { FavoriteUser(it, avatarUrl, false) }
                if (entity != null) favViewModel.addOrDeleteFav(entity, listFavorite.any {
                    it.username == username
                })
            }
            setToggleButton(isFavorited)
        }


    }
    private fun setToggleButton(isFavorited: Boolean) {
        Binding.favToggle.apply {
            if (isFavorited) {
                setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this@UserDetailActivity, R.drawable.baseline_favorite
                    )
                )
            } else {
                setBackgroundDrawable(
                    ContextCompat.getDrawable(
                        this@UserDetailActivity, R.drawable.baseline_favorite_border
                    )
                )
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
    private fun showLoading(state: Boolean){
        if (state) {
            Binding.progressBar2.visibility = View.VISIBLE
        }
        else {
            Binding.progressBar2.visibility = View.GONE
        }
        Binding.tvFollowers.visibility = View.VISIBLE
        Binding.tvFollowing.visibility = View.VISIBLE

    }

    companion object {
        const val EXTRA_NAME = "extra_name"

        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2

        )
    }
}