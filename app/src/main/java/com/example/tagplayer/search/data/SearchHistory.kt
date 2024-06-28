package com.example.tagplayer.search.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tagplayer.search.domain.SearchDomain
import com.example.tagplayer.search.presentation.SearchUi
import java.util.Date

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey
    @ColumnInfo("search_request") val searchRequest: String,
    @ColumnInfo("date") val date: Date
) {
    interface Mapper<T> {
        fun map(searchRequest: String) : T

        object Base : Mapper<SearchDomain> {
            override fun map(searchRequest: String) = SearchDomain(searchRequest)
        }
    }

    fun <T> map(mapper: Mapper<T>) : T = mapper.map(searchRequest)
}