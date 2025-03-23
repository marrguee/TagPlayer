package com.example.tagplayer.home.data

import com.example.tagplayer.FakeForegroundWrapper
import com.example.tagplayer.FakeHandleTry
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.home.domain.OrderType
import com.example.tagplayer.home.domain.ProvideGenerateSql
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.home.domain.errors.HomeException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HomeRepositoryTest {
    private lateinit var repository: HomeRepositoryImpl
    private lateinit var foregroundWrapper: FakeForegroundWrapper
    private lateinit var handleTry: FakeHandleTry<HomeException>
    private lateinit var cacheDatasource: FakeCacheDatasource
    private lateinit var mapper: FakeMapper
    private lateinit var recentlyMapper: FakeRecentlyMapper

    @Before
    fun setup() {
        foregroundWrapper = FakeForegroundWrapper.Base()
        handleTry = FakeHandleTry.Base()
        cacheDatasource = FakeCacheDatasource.Base()
        mapper = FakeMapper.Base()
        recentlyMapper = FakeRecentlyMapper.Base()

        repository = HomeRepositoryImpl(
            foregroundWrapper,
            handleTry,
            cacheDatasource,
            mapper,
            recentlyMapper
        )
    }

    @Test
    fun `scan test`() {
        repository.scan()
        foregroundWrapper.checkScanCalled(1)
    }

    @Test
    fun `play test`() {
        val songId: Long = 1
        repository.play(songId)
        foregroundWrapper.checkPlayCalled(1)
        foregroundWrapper.checkPlayCalledWithId(songId)
    }

    @Test
    fun `recently test`() = runBlocking {
        repository.croppedRecently()
        handleTry.checkHandleAsyncCalled(1)
        cacheDatasource.checkRecentlyCalled(1)
    }

    @Test
    fun `sorted test`() {
        repository.sorted(ObtainFieldName.SongTitle, OrderType.Asc)
        handleTry.checkHandleCalled(1)
        cacheDatasource.checkSortCalled(1)
    }

    private interface FakeCacheDatasource : HomeCacheDatasource {
        fun checkSortCalled(times: Int)
        fun checkRecentlyCalled(times: Int)

        class Base : FakeCacheDatasource {
            private var sortCalled = 0
            private var recentlyCalled = 0

            override fun checkSortCalled(times: Int) {
                assertEquals(times, sortCalled)
            }

            override fun checkRecentlyCalled(times: Int) {
                assertEquals(times, recentlyCalled)
            }

            override fun sorted(field: ObtainFieldName, sql: ProvideGenerateSql): Flow<List<Song>> {
                sortCalled++
                return emptyFlow()
            }

            override suspend fun croppedRecently(): List<SongLastPlayedCrossRef> {
                recentlyCalled++
                return emptyList()
            }
        }
    }

    private interface FakeMapper : Song.Mapper<SongDomain> {
        class Base : FakeMapper {
            override fun map(
                id: Long,
                image: String?,
                title: String,
                author: String?,
                duration: Long,
                dateModified: Long
            ): SongDomain = SongDomain(id, image, title, author, duration)
        }
    }

    private interface FakeRecentlyMapper : SongLastPlayedCrossRef.Mapper<SongDomain> {
        class Base : FakeRecentlyMapper {
            override fun map(list: List<SongLastPlayedCrossRef>): List<SongDomain> =
                emptyList()
        }
    }
}