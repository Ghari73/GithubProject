package com.example.githubproject.usif.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.size
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubproject.R
import com.example.githubproject.adapter.UserAdapter
import com.example.githubproject.data.model.ItemsItem
import com.example.githubproject.databinding.ActivityMainBinding
import com.example.githubproject.usif.detail.UserDetailActivity
import com.example.githubproject.usif.detail.ViewModelFactory
import com.example.githubproject.usif.favorite.FavUserActivity
import com.example.githubproject.usif.setting.SettingActivity
import com.example.githubproject.usif.setting.SettingPreferences
import com.example.githubproject.usif.setting.SettingViewModel
import com.example.githubproject.usif.setting.SettingViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private lateinit var Binding : ActivityMainBinding
    private val viewModel by viewModels<UserViewModel> { ViewModelFactory.getInstance(application) }
    private lateinit var adapter: UserAdapter
    private lateinit var srcQuery: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        viewModel.SearchUsersSetter("ghari")
        showListRV()
        viewModel.getSrcUser().observe(this){
                user -> setList(user)
        }
        viewModel.isLoad().observe(this){
            showLoading(it)
        }
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                srcQuery = query
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
                viewModel.SearchUsersSetter(srcQuery)
                if (Binding.rvUserlist.size == 0) {
                    showErrorMessage(true)
                } else {
                    showErrorMessage(false)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                srcQuery = newText
                viewModel.SearchUsersSetter(srcQuery)
                showErrorMessage(false)
                return true
            }
        })
        return true
    }

    private fun showListRV() {
        adapter = UserAdapter(emptyList())
        Binding.rvUserlist.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        Binding.rvUserlist.layoutManager = layoutManager
        Binding.rvUserlist.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        Binding.rvUserlist.addItemDecoration(itemDecoration)
    }
    private fun setList(data: List<ItemsItem>) {
        adapter = UserAdapter(data)
        Binding.rvUserlist.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                Intent(this@MainActivity, UserDetailActivity::class.java).also {
                    it.putExtra(UserDetailActivity.EXTRA_NAME, data.login)
                    startActivity(it)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favButton -> {
                val intent = Intent(this, FavUserActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.setting -> {
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun showLoading(state: Boolean){
        if (state) View.VISIBLE else View.GONE
    }

    private fun showErrorMessage(errorVisible: Boolean) {
        Binding.IVErrorMSG.visibility = if (errorVisible) View.VISIBLE else View.INVISIBLE
        Binding.tvErrorMSG.visibility = if (errorVisible) View.VISIBLE else View.INVISIBLE
    }

}