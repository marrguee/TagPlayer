package com.example.tagplayer.tag_settings.data

import com.example.tagplayer.core.data.AbstractSongBasedRepository
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tag_settings.domain.TagSettingsDomain
import com.example.tagplayer.tag_settings.domain.TagSettingsRepository
import com.example.tagplayer.tag_settings.domain.errors.TagSettingsException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TagSettingsRepositoryImpl(
    foregroundWrapper: ForegroundWrapper,
    private val handleTry: HandleTry<TagSettingsException>,
    private val cacheDatasource: TagSettingsCacheDatasource,
    private val mapper: SongTag.Mapper<TagSettingsDomain>,
) : AbstractSongBasedRepository(foregroundWrapper), TagSettingsRepository<TagSettingsDomain> {

    override fun tags(): Flow<List<TagSettingsDomain>> = handleTry
        .handle(TagSettingsException.Fetch()) {
            cacheDatasource.tags().map { list -> list.map { it.map(mapper) } }
        }

    override suspend fun remove(id: Long) = handleTry.handleAsync(TagSettingsException.Remove()) {
        cacheDatasource.remove(id)
    }
}