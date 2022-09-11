package ru.mts.data.news.db

import androidx.room.*


@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAll(): List<News>

    @Query("SELECT * FROM news WHERE id = :id")
    fun getById(id: Long): News?

    @Insert
    fun insert(news: News?)

    @Update
    fun update(news: News?)

    @Delete
    fun delete(news: News?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: List<News>)
}