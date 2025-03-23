package com.example.tagplayer.recently.data

import com.example.tagplayer.FakeForegroundWrapper
import com.example.tagplayer.FakeHandleTry
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.recently.domain.RecentlyDomain
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecentlyRepositoryTest {
    private lateinit var repository: RecentlyRepositoryImpl

    private lateinit var foregroundWrapper: FakeForegroundWrapper
    private lateinit var handleTry: FakeHandleTry<Exception>
    private lateinit var mapper: FakeMapper
    private lateinit var cacheDatasource: FakeCacheDatasource

    @Before
    fun setup() {
        foregroundWrapper = FakeForegroundWrapper.Base()
        handleTry = FakeHandleTry.Base()
        mapper = FakeMapper.Base()
        cacheDatasource = FakeCacheDatasource.Base()

        repository = RecentlyRepositoryImpl(
            foregroundWrapper,
            handleTry,
            cacheDatasource,
            mapper
        )
    }

    @Test
    fun `get recently`() = runBlocking {
        val result: List<RecentlyDomain> = repository.recently()
        handleTry.checkHandleAsyncCalled(1)
        cacheDatasource.checkRecentlyCalled(1)
    }

    @Test
    fun play() {
        val songId: Long = 0
        repository.play(songId)
        foregroundWrapper.checkPlayCalled(1)
        foregroundWrapper.checkPlayCalledWithId(songId)
    }

    private interface FakeCacheDatasource : RecentlyCacheDatasource {
        fun checkRecentlyCalled(times: Int)

        class Base : FakeCacheDatasource {
            private var recentlyCalled = 0

            override fun checkRecentlyCalled(times: Int) {
                assertEquals(times, recentlyCalled)
            }

            override suspend fun recently(): List<SongLastPlayedCrossRef> {
                recentlyCalled++
                return emptyList()
            }
        }
    }

    private interface FakeMapper : SongLastPlayedCrossRef.Mapper<RecentlyDomain> {
        class Base : FakeMapper {
            override fun map(list: List<SongLastPlayedCrossRef>): List<RecentlyDomain> =
                emptyList()
        }
    }
}