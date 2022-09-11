package ru.mts.data.news.remote

import com.google.gson.annotations.SerializedName
import ru.mts.data.news.db.News


class NewsDto {

    data class Request(@SerializedName("id") val id: Int)

    data class Response(
        @SerializedName("result") val result: Int,
        @SerializedName("items") val items: List<News>
    )
}