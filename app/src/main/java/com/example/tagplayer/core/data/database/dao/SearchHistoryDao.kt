package com.example.tagplayer.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tagplayer.core.data.database.models.SearchHistoryTable

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history ORDER BY date DESC")
    suspend fun searchHistory() : List<SearchHistoryTable>
    @Insert(entity = SearchHistoryTable::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSearch(searchHistoryTable: SearchHistoryTable)
}