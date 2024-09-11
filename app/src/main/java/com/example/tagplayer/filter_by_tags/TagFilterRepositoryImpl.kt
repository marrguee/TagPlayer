package com.example.tagplayer.filter_by_tags

import com.example.tagplayer.core.data.database.models.SongTag

class TagFilterRepositoryImpl(
    private val cacheDatasource: TagFilterCacheDatasource,
    private val tagModelMapper: SongTag.Mapper<TagFilterDomain>,
) : TagFilterRepository<TagFilterDomain> {

    override suspend fun tags(): List<TagFilterDomain> {
        return cacheDatasource.tags().map { it.map(tagModelMapper) }
    }

    override suspend fun applyFilter(selectedTags: List<Long>) {
        return cacheDatasource.applyFilter(selectedTags)
    }
}