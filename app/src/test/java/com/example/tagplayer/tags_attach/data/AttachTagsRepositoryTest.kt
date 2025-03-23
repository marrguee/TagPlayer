package com.example.tagplayer.tags_attach.data

import com.example.tagplayer.FakeHandleTry
import com.example.tagplayer.tags_attach.domain.AttachTagsRepository
import com.example.tagplayer.tags_attach.domain.TagDomain
import com.example.tagplayer.tags_attach.domain.errors.AttachTagsException
import com.example.tagplayer.core.data.database.models.SongTag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith


@RunWith(Enclosed::class)
class AttachTagsRepositoryTest {

    class SuccessTests {
        private lateinit var repository: AttachTagsRepository<TagDomain>
        private lateinit var cacheDatasource: FakeCacheDatasource
        private lateinit var handleTry: FakeHandleTry<AttachTagsException>

        @Before
        fun setup() {
            cacheDatasource = FakeCacheDatasource.Base()
            handleTry = FakeHandleTry.Base()
            repository = AttachTagsRepositoryImpl(cacheDatasource, handleTry)
        }

        @Test
        fun `get all tags exclude owned`() {
            repository.all(0L)
            handleTry.checkHandleCalled(1)
            cacheDatasource.checkGetAllTagsCalled(1)
        }

        @Test
        fun `get owned tags`() {
            repository.owned(0L)
            handleTry.checkHandleCalled(1)
            cacheDatasource.checkGetOwnedTagsCalled(1)
        }

        @Test
        fun `add tag`() = runBlocking {
            repository.add(0L, 0L)
            handleTry.checkHandleAsyncCalled(1)
            cacheDatasource.checkAddCalled(1)
        }

        @Test
        fun `remove tag`() = runBlocking {
            repository.remove(0L, 0L)
            handleTry.checkHandleAsyncCalled(1)
            cacheDatasource.checkRemoveCalled(1)
        }
    }

    class ErrorTests {
        private lateinit var repository: AttachTagsRepository<TagDomain>
        private lateinit var cacheDatasource: FakeCacheDatasource
        private lateinit var handleTry: FakeHandleTry<AttachTagsException>

        @Before
        fun setup() {
            cacheDatasource = FakeCacheDatasource.Base()
            handleTry = FakeHandleTry.Error()
            repository = AttachTagsRepositoryImpl(cacheDatasource, handleTry)
        }

        @Test(expected = AttachTagsException.All::class)
        fun `get all tags exclude owned`() {
            repository.all(0L)
        }

        @Test(expected = AttachTagsException.Owned::class)
        fun `get owned tags`() {
            repository.owned(0L)
        }

        @Test(expected = AttachTagsException.Add::class)
        fun `add tag`() = runBlocking {
            repository.add(0L, 0L)
        }

        @Test(expected = AttachTagsException.Remove::class)
        fun `remove tag`() = runBlocking {
            repository.remove(0L, 0L)
        }
    }

    private interface FakeCacheDatasource: AttachTagsCacheDatasource {
        fun checkGetAllTagsCalled(expected: Int)
        fun checkGetOwnedTagsCalled(expected: Int)
        fun checkAddCalled(expected: Int)
        fun checkRemoveCalled(expected: Int)

        class Base : FakeCacheDatasource {
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

            override fun all(songId: Long): Flow<List<SongTag>> {
                allCalled++
                return flowOf()
            }

            override fun owned(songId: Long): Flow<List<SongTag>> {
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