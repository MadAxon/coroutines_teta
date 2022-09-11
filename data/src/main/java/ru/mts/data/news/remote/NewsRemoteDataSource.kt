package ru.mts.data.news.remote

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import ru.mts.data.main.NetworkClient
import ru.mts.data.utils.runOperationCatching
import ru.mts.data.utils.Result

class NewsRemoteDataSource {

    // Consider that every second request is going to be cancelled by timeout
    private var lowConnection = false

    suspend fun getNews(): Result<NewsDto.Response, Throwable> {
        val timeout = if (lowConnection) 2500L else 5000L
        lowConnection = !lowConnection
        return runOperationCatching {
            withTimeout(timeout) {
                delay(3000L)
                NetworkClient.create().getSampleData(NewsDto.Request(1))
            }
        }
    }
}