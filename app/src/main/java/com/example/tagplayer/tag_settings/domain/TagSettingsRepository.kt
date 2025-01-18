package com.example.tagplayer.tag_settings.domain

import kotlinx.coroutines.flow.Flow

interface TagSettingsRepository<T> :
    //HandleRepositoryRequest<T, Any>,
    RemoveTag<Long> {
        fun tags() : Flow<List<TagDomain>>
    }