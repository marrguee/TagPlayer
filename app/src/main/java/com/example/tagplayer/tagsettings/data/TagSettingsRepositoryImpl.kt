package com.example.tagplayer.tagsettings.data

import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tagsettings.domain.TagDomain
import com.example.tagplayer.tagsettings.domain.TagSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TagSettingsRepositoryImpl(
    handleError: HandleError<Exception, DomainError>,
    foregroundWrapper: ForegroundWrapper,
    private val cacheDatasource: TagSettingsCacheDatasource.Base,
    private val tagModelMapper: SongTag.Mapper.ToDomain,
    private val tagModelMapperToData: TagDomain.Mapper.ToData,
) : AbstractSongBasedRepository<SongTag, TagDomain, Any>(foregroundWrapper, handleError),
    TagSettingsRepository<TagDomain>
{
    override fun tags(): Flow<List<TagDomain>> {
        return cacheDatasource.tags().map { list -> list.map { it.map(tagModelMapper) } }
    }

    override suspend fun removeTag(id: Long) =
        cacheDatasource.removeTag(id)

}