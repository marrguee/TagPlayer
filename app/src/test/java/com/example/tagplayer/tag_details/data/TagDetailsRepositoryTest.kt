package com.example.tagplayer.tag_details.data

import com.example.tagplayer.FakeHandleTry
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tag_details.domain.errors.TagDetailsException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TagDetailsRepositoryTest {
    private lateinit var repository: TagDetailsRepositoryImpl

    private lateinit var cacheDatasource: FakeCacheDatasource
    private lateinit var handleTry: FakeHandleTry<TagDetailsException>

    private val data = Triple(10L, "title", "color")
    @Before
    fun setup() {
        cacheDatasource = FakeCacheDatasource.Base(SongTag(data.first, data.second, data.third))
        handleTry = FakeHandleTry.Base()

        repository = TagDetailsRepositoryImpl(
            cacheDatasource,
            handleTry
        )
    }

    @Test
    fun `get tag details`() = runBlocking {
        val id = 10L
        val result: SongTag = repository.tag(id)
        assertEquals(result, SongTag(data.first, data.second, data.third))
        cacheDatasource.checkTagCalledWithId(id)
        handleTry.checkHandleAsyncCalled(1)
    }

    @Test
    fun `add or update tag`() = runBlocking {
        repository.add(data.first, data.second, data.third)
        handleTry.checkHandleAsyncCalled(1)
        cacheDatasource.checkAddCalledWithData(data)
    }

    private interface FakeCacheDatasource : TagDetailsDatasource {
        fun checkTagCalledWithId(id: Long)
        fun checkAddCalledWithData(data: Triple<Long, String, String>)

        class Base(
            private val tag: SongTag
        ) : FakeCacheDatasource {
            private var tagId: Long? = null
            private var tagData: Triple<Long, String, String>? = null

            override fun checkTagCalledWithId(id: Long) = assertEquals(id, tagId)

            override fun checkAddCalledWithData(data: Triple<Long, String, String>) =
                assertEquals(data, tagData)

            override suspend fun tag(id: Long): SongTag {
                tagId = id
                return tag
            }

            override suspend fun add(id: Long, title: String, color: String) {
                tagData = Triple(id, title, color)
            }
        }
    }
}