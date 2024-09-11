package com.example.tagplayer.filter_by_tags

interface FilterTagsInteractor {
    suspend fun tags(): List<TagFilterUi>
    suspend fun applyFilter(selectedTags: List<Long>)

    class Base(
        private val repository: TagFilterRepository<TagFilterDomain>,
    ) : FilterTagsInteractor {
        override suspend fun tags(): List<TagFilterUi> {
            return repository.tags().map { it.mapToUi() }
        }

        override suspend fun applyFilter(selectedTags: List<Long>) {
            return repository.applyFilter(selectedTags)
        }
    }
}