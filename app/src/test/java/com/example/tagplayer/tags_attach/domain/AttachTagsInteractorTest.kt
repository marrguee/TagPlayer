package com.example.tagplayer.tags_attach.domain

import com.example.tagplayer.FakeHandleResponse
import com.example.tagplayer.tags_attach.presentation.TagUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AttachTagsInteractorTest {
    private lateinit var interactor: AttachTagsInteractor
    private lateinit var handleResponse: FakeHandleResponse.All<TagsResponse>
    private lateinit var repository: FakeRepository

    @Before
    fun setup() {
        repository = FakeRepository.Base()
        handleResponse = FakeHandleResponse.Empty(TagsResponse.Empty)
        interactor = AttachTagsInteractor.Base(repository, TagDomain.Mapper.Ui, handleResponse)
    }

    @Test
    fun `get combined state with all and owned tags`() {
        val result: TagsResponse = interactor.tags(0L)
        val expected = TagsResponse.Success(
            all = flowOf<List<TagUi>>(),
            owned = flowOf<List<TagUi>>()
        )
        repository.checkGetAllTagsCalled(1)
        repository.checkGetOwnedTagsCalled(1)
        handleResponse.checkHandleCalled(1)
        assertEquals(expected::class, result::class)
    }

    @Test
    fun `add tag`() = runBlocking {
        val result: TagsResponse = interactor.add(0L, 0L)
        repository.checkAddCalled(1)
        handleResponse.checkHandleAsyncEmptyCalled(1)
        assertEquals(TagsResponse.Empty, result)
    }

    @Test
    fun `remove tag`() = runBlocking {
        val result: TagsResponse = interactor.remove(0L, 0L)
        repository.checkRemoveCalled(1)
        handleResponse.checkHandleAsyncEmptyCalled(1)
        assertEquals(TagsResponse.Empty, result)
    }

    private interface FakeRepository : AttachTagsRepository<TagDomain> {
        fun checkGetAllTagsCalled(expected: Int)
        fun checkGetOwnedTagsCalled(expected: Int)
        fun checkAddCalled(expected: Int)
        fun checkRemoveCalled(expected: Int)

        class Base : FakeRepository {
            private var allCalled: Int = 0
            private var ownedCalled: Int = 0
            private var addCalled: Int = 0
            private var removeCalled: Int = 0

            override fun checkGetAllTagsCalled(expected: Int) {
                assertEquals(expected, allCalled)
            }

            override fun checkGetOwnedTagsCalled(expected: Int) {
                assertEquals(expected, ownedCalled)
            }

            override fun checkAddCalled(expected: Int) {
                assertEquals(expected, addCalled)
            }

            override fun checkRemoveCalled(expected: Int) {
                assertEquals(expected, removeCalled)
            }

            override fun all(songId: Long): Flow<List<TagDomain>> {
                allCalled++
                return flowOf()
            }

            override fun owned(songId: Long): Flow<List<TagDomain>> {
                ownedCalled++
                return flowOf()
            }

            override suspend fun add(songId: Long, tagId: Long) {
                addCalled++
            }

            override suspend fun remove(songId: Long, tagId: Long) {
                removeCalled++
            }
        }
    }
}