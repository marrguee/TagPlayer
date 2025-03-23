package com.example.tagplayer.tags_attach.domain

import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.tags_attach.presentation.AttachTagsState
import com.example.tagplayer.tags_attach.presentation.TagUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

interface TagsResponse {
    fun map(mapper: Mapper, scope: CoroutineScope)

    interface Mapper {
        fun mapSuccess(all: Flow<List<TagUi>>, owned: Flow<List<TagUi>>, scope: CoroutineScope)
        fun mapError(error: String)

        class Base(
            private val observable: CustomObservable.UpdateUi<AttachTagsState>,
            private val dispatcherList: DispatcherList,
        ) : Mapper {
            private var job: Job? = null

            override fun mapSuccess(
                all: Flow<List<TagUi>>,
                owned: Flow<List<TagUi>>,
                scope: CoroutineScope
            ) {
                job?.cancel()
                job = scope.launch(dispatcherList.ui()) {
                    all.combine(owned) { allList, ownedList ->
                        AttachTagsState.DragAndDrop(allList, ownedList)
                    }.cancellable().flowOn(dispatcherList.io()).collect {
                        observable.update(it)
                    }
                }
            }

            override fun mapError(error: String) {
                job?.run {
                    cancel()
                    job = null
                }
                observable.update(AttachTagsState.Error(error))
            }
        }
    }

    class Success(
        private val all: Flow<List<TagUi>>,
        private val owned: Flow<List<TagUi>>,
    ) : TagsResponse {
        override fun map(mapper: Mapper, scope: CoroutineScope) =
            mapper.mapSuccess(all, owned, scope)
    }

    object Empty : TagsResponse {
        override fun map(mapper: Mapper, scope: CoroutineScope) = Unit
    }

    class Error(private val error: String) : TagsResponse {
        override fun map(mapper: Mapper, scope: CoroutineScope) = mapper.mapError(error)
    }
}