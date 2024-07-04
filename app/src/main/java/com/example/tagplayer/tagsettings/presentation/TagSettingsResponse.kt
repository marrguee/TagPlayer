package com.example.tagplayer.tagsettings.presentation

import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.search.domain.SearchState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

interface TagSettingsResponse {
    fun map(mapper: TagSettingsResponseMapper, coroutineScope: CoroutineScope)

    interface TagSettingsResponseMapper {
        fun mapSuccess(flow: Flow<List<TagUi>>, coroutineScope: CoroutineScope)
        fun mapError(message: String)

        class Base(
            private val communication: Communication<TagSettingsState>,
            private val dispatcherList: DispatcherList
        ) : TagSettingsResponseMapper {
            override fun mapSuccess(flow: Flow<List<TagUi>>, coroutineScope: CoroutineScope) {
                coroutineScope.launch {
                    flow.flowOn(dispatcherList.io()).collect {
                        communication.update(TagSettingsState.UpdateTagList(it))
                    }
                }

            }

            override fun mapError(message: String) {
                communication.update(TagSettingsState.Error(message))
            }
        }
    }

    class Success(private val flow: Flow<List<TagUi>>) : TagSettingsResponse {
        override fun map(mapper: TagSettingsResponseMapper, coroutineScope: CoroutineScope) {
            mapper.mapSuccess(flow, coroutineScope)
        }
    }

    class Error(private val message: String) : TagSettingsResponse {
        override fun map(mapper: TagSettingsResponseMapper, coroutineScope: CoroutineScope) {
            mapper.mapError(message)
        }
    }
}