package ru.mts.data.news.db

import android.content.Context
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.mts.data.main.AppDatabase
import ru.mts.data.utils.Result
import ru.mts.data.utils.runOperationCatching

class NewsLocalDataSource(private val context: Context) {

    private val appDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    suspend fun getNews(): Result<List<News>, Throwable> {
        return runOperationCatching {
            delay(1000L)
            appDatabase.newsDao().getAll()
        }
    }

    suspend fun saveNews(items: List<News>): Result<Unit, Throwable> {
        return runOperationCatching {
            withContext(NonCancellable) {
                appDatabase.newsDao().insertAll(items)
            }
        }
    }
}
