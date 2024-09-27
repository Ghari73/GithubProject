package com.example.githubproject.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertToFav(favoriteUser: FavoriteUser)

    @Update
    fun update(favoriteUser: FavoriteUser)

    @Delete
    suspend fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM FavUser where isFavorite = 1")
    fun getFavoritedUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM FavUser WHERE login = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<List<FavoriteUser>>
}