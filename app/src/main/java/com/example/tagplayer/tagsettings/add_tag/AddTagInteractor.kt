package com.example.tagplayer.tagsettings.add_tag

interface AddTagInteractor {
    suspend fun addTag(title: String, color: String, id: Long = 0)

    class Base(
        private val repository: AddTagRepository
    ) : AddTagInteractor {
        override suspend fun addTag(title: String, color: String, id: Long) {
            repository.addTag(id, title, color)
        }
    }
}
