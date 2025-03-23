package com.example.tagplayer.recently.domain

import com.example.tagplayer.FakeHandleResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecentlyInteractorTest {
    private lateinit var interactor: RecentlyInteractor
    private lateinit var repository: FakeRepository
    private lateinit var handleResponse: FakeHandleResponse.Handle<RecentlyResponse>

    @Before
    fun setUp() {
        repository = FakeRepository.Base()
        handleResponse = FakeHandleResponse.Base()
        interactor = RecentlyInteractor.Base(repository, handleResponse)
    }

    @Test
    fun `test recently calls repository and handleResponse`() = runBlocking {
        val result: RecentlyResponse = interactor.recently()
        repository.checkRecentlyCalled(1)
        handleResponse.checkHandleAsyncCalled(1)
    }

    @Test
    fun `test play calls repository play`() {
        val songId = 123L
        interactor.play(songId)
        repository.checkPlayCalled(1)
    }

    private interface FakeRepository : RecentlyRepository<RecentlyDomain> {
        fun checkRecentlyCalled(times: Int)
        fun checkPlayCalled(times: Int)

        class Base : FakeRepository {
            private var recentlyCalled = 0
            private var playCalled = 0

            override fun checkRecentlyCalled(times: Int) = assertEquals(times, recentlyCalled)
            override fun checkPlayCalled(times: Int) = assertEquals(times, playCalled)

            override suspend fun recently(): List<RecentlyDomain> {
                recentlyCalled++
                return listOf()
            }

            override fun play(id: Long) {
                playCalled++
            }
        }
    }
}
