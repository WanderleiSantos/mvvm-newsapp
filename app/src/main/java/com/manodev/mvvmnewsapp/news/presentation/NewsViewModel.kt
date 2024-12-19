package com.manodev.mvvmnewsapp.news.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manodev.mvvmnewsapp.core.domain.NewsRepository
import com.manodev.mvvmnewsapp.core.domain.NewsResult
import kotlinx.coroutines.launch

class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var state by mutableStateOf(NewsState())
        private set

    init {
        loadNews()
    }

    fun onAction(action: NewsAction) {
        when (action) {
            NewsAction.Paginate -> {
                paginate()
            }
        }
    }

    private fun loadNews() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )

            newsRepository.getNews().collect { newsResult ->
                state = when (newsResult) {
                    is NewsResult.Error -> {
                        state.copy(isError = true)
                    }

                    is NewsResult.Success -> {
                        state.copy(
                            isError = false,
                            articleList = newsResult.data?.articles ?: emptyList(),
                            nextPage = newsResult.data?.nextPage
                        )
                    }
                }
            }

            state = state.copy(
                isLoading = false
            )
        }
    }

    private fun paginate() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )

            newsRepository.paginate(state.nextPage).collect { newResult ->
                state = when (newResult) {
                    is NewsResult.Error -> {
                        state.copy(isError = true)
                    }

                    is NewsResult.Success -> {
                        val articles = newResult.data?.articles ?: emptyList()
                        state.copy(
                            isError = false,
                            articleList = state.articleList + articles,
                            nextPage = newResult.data?.nextPage
                        )
                    }
                }
            }

            state = state.copy(
                isLoading = false
            )
        }
    }
}