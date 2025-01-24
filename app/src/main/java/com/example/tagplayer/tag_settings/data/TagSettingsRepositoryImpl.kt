package com.example.tagplayer.tag_settings.data

import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tag_settings.domain.TagDomain
import com.example.tagplayer.tag_settings.domain.TagSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TagSettingsRepositoryImpl(
    private val handleError: HandleError<Exception, DomainError>,
    foregroundWrapper: ForegroundWrapper,
    private val cacheDatasource: TagSettingsCacheDatasource.Base,
    private val tagModelMapper: SongTag.Mapper.ToDomain,
) : AbstractSongBasedRepository(foregroundWrapper),
    TagSettingsRepository<TagDomain>
{
    override fun tags(): Flow<List<TagDomain>> = try {
        cacheDatasource.tags().map { list -> list.map { it.map(tagModelMapper) } }
    } catch (e: Exception) {
        throw handleError.handle(e)
    }

    override suspend fun removeTag(id: Long) =
        cacheDatasource.removeTag(id)

}