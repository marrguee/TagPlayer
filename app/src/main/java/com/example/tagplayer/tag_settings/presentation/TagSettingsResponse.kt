package com.example.tagplayer.tag_settings.presentation

import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.domain.DispatcherList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

interface TagSettingsResponse {
    fun map(mapper: Mapper, coroutineScope: CoroutineScope)

    interface Mapper {
        fun mapSuccess(flow: Flow<List<TagSettingsUi>>, coroutineScope: CoroutineScope)
        fun mapError(error: String)

        class Base(
            private val observable: CustomObservable.UpdateUi<TagSettingsState>,
            private val dispatcherList: DispatcherList
        ) : Mapper {
            private var job : Job? = null

            override fun mapSuccess(
                flow: Flow<List<TagSettingsUi>>,
                coroutineScope: CoroutineScope
            ) {
                job?.cancel()
                job = coroutineScope.launch(dispatcherList.ui()) {
                    flow.cancellable().flowOn(dispatcherList.io()).collect {
                        observable.update(TagSettingsState.UpdateTags(it))
                    }
                }
            }

            override fun mapError(error: String) =
                observable.update(TagSettingsState.Error(error))
        }
    }

    class Success(private val flow: Flow<List<TagSettingsUi>>) : TagSettingsResponse {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) =
            mapper.mapSuccess(flow, coroutineScope)
    }

    object Empty : TagSettingsResponse {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) = Unit
    }

    class Error(private val message: String) : TagSettingsResponse {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) =
            mapper.mapError(message)
    }
}