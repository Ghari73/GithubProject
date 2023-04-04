package com.example.githubproject.usif.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubproject.R
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
        println("panjang list user COBSZ : "+viewModel._listUser.value?.size)

//        adapter.notifyDataSetChanged()


//        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)

        adapter = UserAdapter(emptyList())
        viewModel.SearchUsersSetter("ghari")
        println("panjang list user 1 : "+viewModel.listUser.value?.size)

        Binding.apply {
            rvUserlist.adapter = adapter
            rvUserlist.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUserlist.setHasFixedSize(true)

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
        supportActionBar?.apply {
            title = adapter.itemCount.toString()
            setDisplayHomeAsUpEnabled(true)
        }
        viewModel._listUser.observe(this){
                user -> Log.d("anjayanai",user.toString())
            setList(user)
        }


        viewModel._isLoading.observe(this){
            showLoading(it)
        }

    }

    private fun setList(data: List<ItemsItem>) {
        adapter = UserAdapter(data)
        Binding.rvUserlist.adapter = adapter
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