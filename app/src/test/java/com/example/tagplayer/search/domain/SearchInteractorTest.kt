package com.example.tagplayer.search.domain

import com.example.tagplayer.FakeHandleResponse
import com.example.tagplayer.search.presentation.SearchUi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchInteractorTest {
    private lateinit var interactor: SearchInteractor

    private lateinit var repository: FakeRepository
    private lateinit var handleResponse: FakeHandleResponse.Handle<SearchResponse>
    private lateinit var mapper: FakeMapper

    @Before
    fun setup() {
        repository = FakeRepository.Base()
        handleResponse = FakeHandleResponse.Base()
        mapper = FakeMapper.Base()

        interactor = SearchInteractor.Base(repository, handleResponse, mapper)
    }

    @Test
    fun `test play calls repository`() {
        val songId = 42L
        interactor.play(songId)
        repository.checkPlayCalledWithId(songId)
    }

    @Test
    fun `test search calls repository`() = runBlocking {
        val query = "test"
        val result: SearchResponse = interactor.search(query)
        repository.checkSearchCalledWithQuery(query)
        handleResponse.checkHandleAsyncCalled(1)
    }

    private interface FakeRepository : SearchRepository<SearchDomain> {
        fun checkPlayCalledWithId(id: Long)
        fun checkSearchCalledWithQuery(query: String)

        class Base : FakeRepository {
            private var songId = 0L
            private var cacheQuery = String()

            override fun play(id: Long) {
                songId = id
            }

            override fun checkPlayCalledWithId(id: Long) = assertEquals(id, songId)

            override fun checkSearchCalledWithQuery(query: String) = assertEquals(query, cacheQuery)

            override suspend fun search(query: String): List<SearchDomain> {
                cacheQuery = query
                return emptyList()
            }
        }
    }

    private interface FakeMapper : SearchDomain.Mapper<SearchUi> {
        class Base : FakeMapper {
            override fun map(
                id: Long,
                thumbnail: String?,
                title: String,
                author: String?,
                duration: Long
            ): SearchUi = SearchUi(id, thumbnail, title, author.toString(), duration.toString())
        }
    }
}