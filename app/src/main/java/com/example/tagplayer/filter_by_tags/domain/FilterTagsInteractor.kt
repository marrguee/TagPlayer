package com.example.tagplayer.filter_by_tags.domain

import com.example.tagplayer.filter_by_tags.presentation.FilterUi

interface FilterTagsInteractor {
    suspend fun tags(): List<FilterUi>
    suspend fun applyFilter(selectedTags: List<Long>)

    class Base(
        private val repository: TagFilterRepository<TagFilterDomain>,
    ) : FilterTagsInteractor {
        override suspend fun tags(): List<FilterUi> {
            return repository.tags().map { it.mapToUi() }
        }

        override suspend fun applyFilter(selectedTags: List<Long>) {
            return repository.applyFilter(selectedTags)
        }
    }
}