package com.example.tagplayer.playback_control.domain

import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.playback_control.presentation.PlayState
import com.example.tagplayer.playback_control.presentation.TagPlaybackUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


interface TagsResult {
    fun map(mapper: Mapper, coroutineScope: CoroutineScope)

    interface Mapper {
        fun mapFlow(flow: Flow<List<TagPlaybackUi>>, coroutineScope: CoroutineScope)
        fun mapError(error:String)

        class Base(
            private val observable: CustomObservable.UpdateUi<PlayState>,
            private val dispatcherList: DispatcherList
        ): Mapper {
            private var job: Job? = null

            override fun mapFlow(flow: Flow<List<TagPlaybackUi>>, coroutineScope: CoroutineScope) {
                job?.let {
                    it.cancel()
                    job = null
                }
                job = coroutineScope.launch {
                    flow.collect {
                        withContext(dispatcherList.ui()){
                            observable.update(PlayState.UpdateTags(it))
                        }
                    }
                }
            }

            override fun mapError(error: String) {
                observable.update(PlayState.Error(error))
            }
        }
    }

    class TagsFlow(
        private val tags: Flow<List<TagPlaybackUi>>
    ) : TagsResult {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) {
            mapper.mapFlow(tags, coroutineScope)
        }
    }

    class Error(
        private val error: String
    ) : TagsResult {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) {
            mapper.mapError(error)
        }
    }
}