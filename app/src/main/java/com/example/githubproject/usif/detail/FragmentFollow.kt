package com.example.githubproject.usif.detail

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubproject.R
import com.example.githubproject.data.model.ItemsItem
import com.example.githubproject.databinding.FragmentFollowBinding
import com.example.githubproject.adapter.UserAdapter
import com.example.githubproject.usif.setting.SettingPreferences
import com.example.githubproject.usif.setting.SettingViewModel
import com.example.githubproject.usif.setting.SettingViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class FragmentFollow: Fragment(R.layout.fragment_follow) {
    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<FollowViewModel>()
    private lateinit var adapter: UserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pos = arguments?.getInt(ARG_POSITION,0)
        val username = arguments?.getString(ARG_USERNAME)

        _binding = FragmentFollowBinding.bind(view)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                binding.IVErrorMSG.setImageResource(R.drawable.baseline_no_accounts_white)
            } else {
                binding.IVErrorMSG.setImageResource(R.drawable.baseline_no_accounts_24)
            }
        }

        if (pos == 1){
            viewModel.setFollowers("$username")
            showListRV()
            viewModel.getFollowers().observe(viewLifecycleOwner){
                user -> setList(user)
                if (user != null){
                    binding.apply {
                        if (user.isEmpty()) {
                            binding.tvErrorMSG.text = String.format("This user has no follower")
                            showErrorMSG(true)
                        }else{
                            showErrorMSG(false)
                        }
                    }
                }
            }
            viewModel.isLoad().observe(viewLifecycleOwner){
                showLoading(it)
            }



        }else{
            viewModel.setFollowing("$username")
            showListRV()
            viewModel.getFollowing().observe(viewLifecycleOwner){
                    user -> setList(user)
                if (user != null){
                    binding.apply {
                        if (user.isEmpty()) {
                            binding.tvErrorMSG.text = String.format("This user doesn't follow anyone")
                            showErrorMSG(true)
                        }else{
                            showErrorMSG(false)
                        }
                    }
                }
            }
            viewModel.isLoad().observe(viewLifecycleOwner){
                showLoading(it)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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

    private fun setList(data: List<ItemsItem>) {
        adapter = UserAdapter(data)
        _binding?.rvUserlist?.adapter = adapter
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