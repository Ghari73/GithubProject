package com.example.githubproject.usif.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubproject.R
import com.example.githubproject.data.model.ItemsItem
import com.example.githubproject.databinding.FragmentFollowBinding
import com.example.githubproject.usif.main.UserAdapter

class FragmentFollow: Fragment(R.layout.fragment_follow) {
    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<FollowViewModel>()
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pos = arguments?.getInt(ARG_POSITION,0)
        val username = arguments?.getString(ARG_USERNAME)

        _binding = FragmentFollowBinding.bind(view)
        if (pos == 1){
            viewModel.setFollowers("$username")
            showListRV()
            viewModel.getFollowers().observe(viewLifecycleOwner){
                user -> setList(user, pos)
            }
            viewModel.isLoad().observe(viewLifecycleOwner){
                showLoading(it)
            }

        }else{
            viewModel.setFollowing("$username")
            showListRV()
            viewModel.getFollowing().observe(viewLifecycleOwner){
                    user -> setList(user, pos)
            }
            viewModel.isLoad().observe(viewLifecycleOwner){
                showLoading(it)
            }
        }
    }

    private fun showListRV(){
        adapter = UserAdapter(emptyList())
        adapter.notifyDataSetChanged()

        binding.apply {
            rvUserlist.setHasFixedSize(true)
            rvUserlist.layoutManager = LinearLayoutManager(activity)
            rvUserlist.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showLoading(state: Boolean)= if (state) View.VISIBLE else View.GONE

    private fun setList(data: List<ItemsItem>, pos : Int?) {
        adapter = UserAdapter(data)
        _binding?.rvUserlist?.adapter = adapter
        if (data.size == 0) {
            binding.tvErrorMSG.text = {
                if (pos == 1) String.format("No Follower Found")
                else String.format("No Following Found")
            }.toString()
            showErrorMSG(true)

        } else {
            showErrorMSG(false)
        }
    }

    private fun showErrorMSG(errorVisible: Boolean) {
        binding.IVErrorMSG.visibility = if (errorVisible) View.VISIBLE else View.INVISIBLE
        binding.tvErrorMSG.visibility = if (errorVisible) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}