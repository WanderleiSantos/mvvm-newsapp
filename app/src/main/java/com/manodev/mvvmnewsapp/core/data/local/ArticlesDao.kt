package com.manodev.mvvmnewsapp.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ArticlesDao {

    @Upsert
    suspend fun upsertArticleList(articleList: List<ArticleEntity>)

    @Query("SELECT * FROM articleentity")
    suspend fun getArticleList(): List<ArticleEntity>

    @Query("SELECT * FROM articleentity WHERE articleId = :articleId")
    suspend fun getArticle(articleId: String): ArticleEntity?

    @Query("DELETE FROM articleentity")
    suspend fun clearDatabase()
}