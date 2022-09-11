package ru.ermolnik.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.mts.data.news.db.News
import ru.mts.data.news.repository.NewsRepository
import ru.mts.data.utils.Result
import ru.mts.data.utils.doOnError
import ru.mts.data.utils.doOnSuccess

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _state: MutableStateFlow<NewsState> = MutableStateFlow(NewsState.Loading)
    val state = _state.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    init {
        viewModelScope.launch {
            getNews(false)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            getNews(true)
            _isRefreshing.emit(false)
        }
    }

    fun retry() {
        viewModelScope.launch {
            _state.emit(NewsState.Loading)
            getNews(true)
        }
    }

    private suspend fun getNews(forceUpdate: Boolean) {
        repository.getNews(forceUpdate).collect {
            it.doOnError { error ->
                _state.emit(NewsState.Error(error))
            }.doOnSuccess { news ->
                _state.emit(NewsState.Content(news))
            }
        }
    }

}
