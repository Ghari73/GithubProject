package com.example.githubproject.usif.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubproject.data.model.ItemsItem
import com.example.githubproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var Binding : ActivityMainBinding
    private val viewModel by viewModels<UserViewModel>()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(Binding.root)
        viewModel.SearchUsersSetter("ghari")

        adapter = UserAdapter(emptyList())
        adapter.notifyDataSetChanged()
        //viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        Binding.apply {
            userList.layoutManager = LinearLayoutManager(this@MainActivity)
            userList.setHasFixedSize(true)
            userList.adapter = adapter

            searchBtn.setOnClickListener {
                searchUser()
            }
            searchBar.setOnKeyListener { view, i, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }

        }

        viewModel.getSrcUser().observe(this){
            user -> setList(user)
        }

        viewModel.isLoad().observe(this){
            showLoading(it)
        }

    }

    private fun setList(data: List<ItemsItem>) {
        adapter = UserAdapter(data)
        Binding.userList.adapter = adapter
//        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: ItemsItem) {
//                Intent(this@MainActivity, DetailUserActivity::class.java).also {
//                    it.putExtra(DetailUserActivity.EXTRA_NAME, data.login)
//                    startActivity(it)
//                }
//            }
//        })
    }
    private fun searchUser(){
        Binding.apply {
            val query = searchBar.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.SearchUsersSetter(query)
        }
    }

    private fun showLoading(state: Boolean){
        if(state){
            Binding.progressBar.visibility = View.VISIBLE
        }else{
            Binding.progressBar.visibility = View.GONE
        }
    }


}