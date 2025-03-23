package com.example.tagplayer.tags_attach.data

import com.example.tagplayer.tags_attach.domain.AttachTagsRepository
import com.example.tagplayer.tags_attach.domain.TagDomain
import com.example.tagplayer.tags_attach.domain.errors.AttachTagsException
import com.example.tagplayer.core.data.HandleTry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AttachTagsRepositoryImpl(
    private val cacheDatasource: AttachTagsCacheDatasource,
    private val handleTry: HandleTry<AttachTagsException>,
) : AttachTagsRepository<TagDomain> {

    override fun all(songId: Long): Flow<List<TagDomain>> = handleTry
        .handle(AttachTagsException.All()) {
            cacheDatasource.all(songId).map {
                list -> list.map { TagDomain(it.id, it.title, it.color) }
            }
        }

    override fun owned(songId: Long): Flow<List<TagDomain>> = handleTry
        .handle(AttachTagsException.Owned()) {
            cacheDatasource.owned(songId).map {
                 list -> list.map { TagDomain(it.id, it.title, it.color) }
            }
        }

    override suspend fun add(songId: Long, tagId: Long) = handleTry
        .handleAsync(AttachTagsException.Add()) {
            cacheDatasource.add(songId, tagId)
        }

    override suspend fun remove(songId: Long, tagId: Long) = handleTry
        .handleAsync(AttachTagsException.Remove()) {
            cacheDatasource.remove(songId, tagId)
        }
}