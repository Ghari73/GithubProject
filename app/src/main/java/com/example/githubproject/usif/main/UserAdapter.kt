package com.example.githubproject.usif.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubproject.data.model.ItemsItem
import com.example.githubproject.databinding.ListUserBinding

class UserAdapter(private val list: List<ItemsItem>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    inner class UserViewHolder(private val binding: ListUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: ItemsItem){
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(user)
            }

            binding.apply {
                userName.text = user.login
                Glide.with(itemView.context).load(user.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(userPic)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ListUserBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.bind(list[position])
    }
}