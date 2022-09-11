package ru.mts.data.news.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.mts.data.news.db.News
import ru.mts.data.news.db.NewsLocalDataSource
import ru.mts.data.news.remote.NewsRemoteDataSource
import ru.mts.data.utils.Result
import ru.mts.data.utils.doOnError
import ru.mts.data.utils.doOnSuccess
import ru.mts.data.utils.getOrThrow

class NewsRepository(
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource
) {
    suspend fun getNews(forceUpdate: Boolean): Flow<Result<List<News>, Throwable>> {
        return flow {
            if (forceUpdate) {
                getNewsFromRemote()
            } else {
                newsLocalDataSource.getNews()
                    .doOnError {
                        getNewsFromRemote()
                    }
                    .doOnSuccess {
                        if (it.isEmpty()) getNewsFromRemote() else emit(Result.Success(it))
                    }
            }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<Result<List<News>, Throwable>>.getNewsFromRemote() = newsRemoteDataSource.getNews()
        .doOnError {
            emit(Result.Error(it))
        }
        .doOnSuccess {
            newsLocalDataSource.saveNews(it.items)
            emit(Result.Success(it.items))
        }

}
