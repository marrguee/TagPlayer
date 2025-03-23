package com.example.tagplayer.playback.domain

import android.content.IntentSender
import android.net.Uri
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.presentation.ShareRequest
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.playback.presentation.PlaybackState
import com.example.tagplayer.playback.presentation.PlaybackState.DeletingSuccess
import com.example.tagplayer.playback.presentation.PlaybackState.PermissionRequired
import com.example.tagplayer.playback.presentation.TagPlaybackUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface SongDetailsResponse {
    fun map(mapper: Mapper, coroutineScope: CoroutineScope)

    interface Mapper {
        fun mapUri(uri: Uri)
        fun mapFlow(flow: Flow<List<TagPlaybackUi>>, coroutineScope: CoroutineScope)
        fun mapError(error:String)
        fun mapIntentSender(intentSender: IntentSender)
        fun mapSucceed()

        class Base(
            private val shareRequest: ShareRequest,
            private val observable: CustomObservable.UpdateUi<PlaybackState>,
            private val dispatcherList: DispatcherList,
        ): Mapper {
            private var job: Job? = null

            override fun mapUri(uri: Uri) = shareRequest.share(uri)

            override fun mapFlow(flow: Flow<List<TagPlaybackUi>>, coroutineScope: CoroutineScope) {
                job?.cancel()
                job = coroutineScope.launch {
                    flow.collect {
                        withContext(dispatcherList.ui()){
                            observable.update(PlaybackState.UpdateTags(it))
                        }
                    }
                }
            }

            override fun mapError(error: String) {
                observable.update(PlaybackState.Error(error))
            }

            override fun mapIntentSender(intentSender: IntentSender) =
                observable.update(PermissionRequired(intentSender))

            override fun mapSucceed() = observable.update(DeletingSuccess)
        }
    }

    data class SongUri(private val uri: String) : SongDetailsResponse {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) =
            mapper.mapUri(Uri.parse(uri))
    }

    data class TagsFlow(private val tags: Flow<List<TagPlaybackUi>>) : SongDetailsResponse {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) =
            mapper.mapFlow(tags, coroutineScope)
    }

    data class DeletingWithPermission(private val intentSender: IntentSender): SongDetailsResponse {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) =
            mapper.mapIntentSender(intentSender)
    }

    object DeletionSucceed: SongDetailsResponse {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) = mapper.mapSucceed()
    }

    data class Error(private val error: String) : SongDetailsResponse {
        override fun map(mapper: Mapper, coroutineScope: CoroutineScope) =
            mapper.mapError(error)
    }
}