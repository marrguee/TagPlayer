package com.example.tagplayer.tag_details.domain

import com.example.tagplayer.FakeHandleResponse
import com.example.tagplayer.core.data.database.models.SongTag
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class TagDetailsInteractorTest {
    private lateinit var interactor: TagDetailsInteractor

    private lateinit var repository: FakeRepository
    private lateinit var handleResponse: FakeHandleResponse.All<TagDetailsResponse>

    private val data = Triple(10L, "title", "color")

    @Before
    fun setUp() {
        repository = FakeRepository.Base(SongTag(data.first, data.second, data.third))
        handleResponse = FakeHandleResponse.Empty(TagDetailsResponse.Empty)

        interactor = TagDetailsInteractor.Base(
            repository,
            handleResponse
        )
    }

    @Test
    fun `test tag method`() = runBlocking {
        val result = interactor.tag(data.first)

        repository.checkTagCalledWithId(data.first)
        handleResponse.checkHandleAsyncCalled(1)

        assertEquals(TagDetailsResponse.Success(data.second, data.third), result)
    }

    @Test
    fun `test add method`() = runBlocking {
        val result = interactor.add(data.second, data.third, data.first)

        repository.checkAddCalledWithData(data)
        handleResponse.checkHandleAsyncEmptyCalled(1)

        assertEquals(TagDetailsResponse.Empty, result)
    }

    private interface FakeRepository : TagDetailsRepository {
        fun checkTagCalledWithId(id: Long)
        fun checkAddCalledWithData(data: Triple<Long, String, String>)

        class Base(
            private val tag: SongTag
        ) : FakeRepository {
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