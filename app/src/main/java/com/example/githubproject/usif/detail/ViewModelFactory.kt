package com.example.githubproject.usif.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubproject.di.Injection
import com.example.githubproject.repository.FavUserRepository
import com.example.githubproject.usif.favorite.FavViewModel
import com.example.githubproject.usif.main.UserViewModel

class ViewModelFactory private constructor(private val favUserRepository: FavUserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            return FavViewModel(favUserRepository) as T
        } else if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(favUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}