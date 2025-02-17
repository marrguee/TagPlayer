package com.example.tagplayer.home.data

import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.models.Song
import kotlinx.coroutines.flow.Flow

interface HandleSort {
    fun sortTitleAsc(): Flow<List<Song>>
    fun sortTitleDesc(): Flow<List<Song>>
    fun sortDateAsc(): Flow<List<Song>>
    fun sortDateDesc(): Flow<List<Song>>

    fun sortTagsAscTitle(tags: List<Long>): Flow<List<Song>>
    fun sortTagsTitleDesc(tags: List<Long>): Flow<List<Song>>
    fun sortTagsDateAsc(tags: List<Long>): Flow<List<Song>>
    fun sortTagsDateDesc(tags: List<Long>): Flow<List<Song>>

     class Base(private val songsDao: SongsDao) : HandleSort {
         override fun sortTitleAsc(): Flow<List<Song>> =
             songsDao.songsSortByTitle(true)

         override fun sortTitleDesc(): Flow<List<Song>>  =
             songsDao.songsSortByTitle(false)

         override fun sortDateAsc(): Flow<List<Song>> =
             songsDao.songsSortByDate(true)

         override fun sortDateDesc(): Flow<List<Song>> =
             songsDao.songsSortByDate(false)

         override fun sortTagsAscTitle(tags: List<Long>): Flow<List<Song>> =
             songsDao.songsTagsSortByTitle(tags, tags.size,true)

         override fun sortTagsTitleDesc(tags: List<Long>): Flow<List<Song>> =
             songsDao.songsTagsSortByTitle(tags, tags.size,false)

         override fun sortTagsDateAsc(tags: List<Long>): Flow<List<Song>> =
             songsDao.songsTagsSortByDate(tags, tags.size,true)

         override fun sortTagsDateDesc(tags: List<Long>): Flow<List<Song>> =
             songsDao.songsTagsSortByDate(tags, tags.size,false)
     }
}