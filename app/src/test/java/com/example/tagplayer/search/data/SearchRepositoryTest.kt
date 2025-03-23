package com.example.tagplayer.search.data

import com.example.tagplayer.FakeForegroundWrapper
import com.example.tagplayer.FakeHandleTry
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.search.domain.SearchDomain
import com.example.tagplayer.search.domain.SearchRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchRepositoryTest {
    private lateinit var repository: SearchRepository<SearchDomain>

    private lateinit var foregroundWrapper: FakeForegroundWrapper
    private lateinit var handleTry: FakeHandleTry<Exception>
    private lateinit var cacheDatasource: FakeCacheDatasource
    private lateinit var mapper: FakeMapper

    @Before
    fun setup() {
        foregroundWrapper = FakeForegroundWrapper.Base()
        handleTry = FakeHandleTry.Base()
        cacheDatasource = FakeCacheDatasource.Base()
        mapper = FakeMapper.Base()

        repository = SearchRepositoryImpl(
            foregroundWrapper,
            handleTry,
            cacheDatasource,
            mapper
        )
    }

    @Test
    fun search() = runBlocking {
        val query = "query"
        val result: List<SearchDomain> = repository.search(query)
        handleTry.checkHandleAsyncCalled(1)
        cacheDatasource.checkSearchCalledWithQuery(query)
    }

    @Test
    fun play() {
        val songId: Long = 1
        repository.play(songId)
        foregroundWrapper.checkPlayCalled(1)
        foregroundWrapper.checkPlayCalledWithId(songId)
    }

    private interface FakeCacheDatasource : SearchCacheDatasource {
        fun checkSearchCalledWithQuery(query: String)

        class Base : FakeCacheDatasource {
            private var cachedQuery = String()

            override fun checkSearchCalledWithQuery(query: String) {
                assertEquals(query, cachedQuery)
            }

            override suspend fun search(query: String): List<Song> {
                cachedQuery = query
                return emptyList()
            }
        }
    }

    private interface FakeMapper : Song.Mapper<SearchDomain> {
        class Base : FakeMapper {
            override fun map(
                id: Long,
                image: String?,
                title: String,
                author: String?,
                duration: Long,
                dateModified: Long
            ): SearchDomain = SearchDomain(id, image, title, author, duration)
        }
    }
}