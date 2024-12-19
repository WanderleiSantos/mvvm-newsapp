package com.manodev.mvvmnewsapp.core.data

import com.manodev.mvvmnewsapp.core.data.local.ArticlesDao
import com.manodev.mvvmnewsapp.core.data.remote.NewsListDto
import com.manodev.mvvmnewsapp.core.domain.Article
import com.manodev.mvvmnewsapp.core.domain.NewsList
import com.manodev.mvvmnewsapp.core.domain.NewsRepository
import com.manodev.mvvmnewsapp.core.domain.NewsResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepositoryImpl(
    private val httpClient: HttpClient,
    private val dao: ArticlesDao
) : NewsRepository {

    private val tag = "NewsRepository: "
    private val baseUrl = "https://newsdata.io/api/1/latest"
    private val apiKey = "xxxx"

    private suspend fun getLocalNews(nextPage: String?): NewsList {
        val localNews = dao.getArticleList()

        val newsList = NewsList(
            articles = localNews.map { it.toArticle() },
            nextPage = nextPage
        )

        return newsList
    }

    private suspend fun getRemoteNews(nextPage: String?): NewsList {
        val newsListDto: NewsListDto = httpClient.get(baseUrl) {
            parameter("apikey", apiKey)
            parameter("language", "en")
            parameter("page", nextPage)
        }.body()

        return newsListDto.toNewsList()
    }

    override suspend fun getNews(): Flow<NewsResult<NewsList>> {
        return flow {

            val remoteNewsList = try {
                getRemoteNews(null)
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                println(tag + "getNews remote exception: " + e.message)
                null
            }

            remoteNewsList?.let {
                dao.clearDatabase()
                dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity() })
                emit(NewsResult.Success(getLocalNews(remoteNewsList.nextPage)))
                return@flow
            }

            val localNewsList = getLocalNews(null)
            if (localNewsList.articles.isNotEmpty()) {
                emit(NewsResult.Success(localNewsList))
                return@flow
            }

            emit(NewsResult.Error("No Data"))
        }
    }

    override suspend fun paginate(nextPage: String?): Flow<NewsResult<NewsList>> {
        return flow {
            val remoteNewsList = try {
                getRemoteNews(nextPage)
            } catch (e: Exception) {
                e.printStackTrace()
                if (e is CancellationException) throw e
                println(tag + "paginate remote exception: " + e.message)
                null
            }

            remoteNewsList?.let {
                dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity() })
                emit(NewsResult.Success(remoteNewsList))
                return@flow
            }
        }
    }

    override suspend fun getArticle(articleId: String): Flow<NewsResult<Article>> {
        TODO("Not yet implemented")
    }
}