package com.example.tagplayer.tagsettings.domain

import com.example.tagplayer.core.data.HandleRepositoryRequest

interface TagSettingsRepository<T> :
    HandleRepositoryRequest<T, Any>,
    RemoveTag<Long>,
    AddTag<T>