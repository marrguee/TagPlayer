package com.example.tagplayer.core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongTag

@Dao
interface SongsAndTagsDao{
    suspend fun tagsBySongId(songId: Long)
    @Query(
        "SELECT * FROM songs INNER JOIN songs_and_tags " +
        "ON songs_and_tags.track_id = songs.id " +
        "AND songs_and_tags.tag_id IN (:tags)"
    )
    suspend fun songsByTagsId(tags: List<SongTag>) : List<Song>
}