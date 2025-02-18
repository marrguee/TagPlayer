package com.example.tagplayer.tag_details.domain

import com.example.tagplayer.tag_details.presentation.TagDialogState
import com.example.tagplayer.core.presentation.observable.CustomObservable

interface TagDetailsResponse {
    fun map(mapper: Mapper)

    interface Mapper {
        fun mapSuccess(title: String, color: String)
        fun mapError(error: String)

        class Base(
            private val observable: CustomObservable.UpdateUi<TagDialogState>,
        ) : Mapper {
            override fun mapSuccess(title: String, color: String) =
                observable.update(TagDialogState.EditMode(title, color))

            override fun mapError(error: String) = observable.update(TagDialogState.Error(error))
        }
    }

    data class Success(
        private val title: String,
        private val color: String,
    ) : TagDetailsResponse {
        override fun map(mapper: Mapper) = mapper.mapSuccess(title, color)
    }

    object Empty : TagDetailsResponse {
        override fun map(mapper: Mapper) = Unit
    }

    class Error(private val error: String) : TagDetailsResponse {
        override fun map(mapper: Mapper) = mapper.mapError(error)
    }
}