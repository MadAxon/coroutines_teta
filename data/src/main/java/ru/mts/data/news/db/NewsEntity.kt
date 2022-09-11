package ru.mts.data.news.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "news")
data class News(
    @PrimaryKey
    @ColumnInfo val id: Int,
    @ColumnInfo val type: NewsType,
    @ColumnInfo val title: String,
    @ColumnInfo val subtitle: String,
    @ColumnInfo val timestamp: String
)

enum class NewsType(val value: String) {
    @SerializedName("finance") FINANCE("finance"),
    @SerializedName("weather") WEATHER("weather"),
    @SerializedName("politics") POLITICS("politics"),
    @SerializedName("science") SCIENCE("science")
}