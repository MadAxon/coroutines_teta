package ru.ermolnik.news

import ru.mts.data.news.db.News

sealed class NewsState {
    object Loading: NewsState()
    data class Error(val throwable: Throwable): NewsState()
    data class Content(val items: List<News>): NewsState()
}