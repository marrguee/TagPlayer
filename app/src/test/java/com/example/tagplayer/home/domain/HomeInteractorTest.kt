package com.example.tagplayer.home.domain

import com.example.tagplayer.FakeHandleResponse
import com.example.tagplayer.home.data.ObtainFieldName
import com.example.tagplayer.home.presentation.SongUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HomeInteractorTest {
    private lateinit var interactor: HomeInteractor
    private lateinit var repository: FakeRepository
    private lateinit var handleResponse: FakeHandleResponse.Handle<SongsResponse>
    private lateinit var mapper: FakeSongMapper
    private lateinit var sortMapper: FakeSortMapper

    @Before
    fun setUp() {
        repository = FakeRepository.Base()
        handleResponse = FakeHandleResponse.Base()
        mapper = FakeSongMapper.Base
        sortMapper = FakeSortMapper.Base(repository)
        interactor = HomeInteractor.Base(repository, handleResponse, mapper, sortMapper)
    }

    @Test
    fun `test croppedRecently returns Recently response`() = runBlocking {
        val result: SongsResponse = interactor.croppedRecently()
        repository.checkRecentlyCalled(1)
        handleResponse.checkHandleAsyncCalled(1)
    }

    @Test
    fun `test sortedSongs returns Library response`() {
        val sortingType = FakeSortType.Base(ObtainFieldName.SongTitle, OrderType.Asc)
        val result: SongsResponse = interactor.sortedSongs(sortingType)
        handleResponse.checkHandleCalled(1)
        repository.checkSortedCalled(1)
    }

    @Test
    fun `test play calls repository play`() {
        val songId = 123L
        interactor.play(songId)
        repository.checkPlayCalled(1)
    }

    @Test
    fun `test scan calls repository scan`() {
        interactor.scan()
        repository.checkScanCalled(1)
    }

    private interface FakeRepository : HomeRepository<SongDomain> {
        fun checkRecentlyCalled(times: Int)
        fun checkSortedCalled(times: Int)
        fun checkPlayCalled(times: Int)
        fun checkScanCalled(times: Int)

        class Base : FakeRepository {
            private var recentlyCalled = 0
            private var sortedCalled = 0
            private var playCalled = 0
            private var scanCalled = 0

            override fun checkRecentlyCalled(times: Int) = assertEquals(times, recentlyCalled)

            override fun checkSortedCalled(times: Int) = assertEquals(times, sortedCalled)

            override fun checkPlayCalled(times: Int) = assertEquals(times, playCalled)

            override fun checkScanCalled(times: Int) = assertEquals(times, scanCalled)

            override suspend fun croppedRecently(): List<SongDomain> {
                recentlyCalled++
                return listOf()
            }

            override fun sorted(field: ObtainFieldName, order: OrderType): Flow<List<SongDomain>> {
                sortedCalled++
                return flowOf()
            }

            override fun play(id: Long) {
                playCalled++
            }

            override fun scan() {
                scanCalled++
            }
        }
    }

    private interface FakeSongMapper : SongDomain.Mapper<SongUi> {
        object Base : FakeSongMapper {
            override suspend fun map(
                id: Long,
                thumbnail: String?,
                title: String,
                author: String?,
                duration: Long
            ): SongUi = SongUi(id, thumbnail, title, author?:String(), String())
        }
    }

    private interface FakeSortMapper : SortType.Mapper<SongUi> {
        class Base(
            private val repository: FakeRepository
        ) : FakeSortMapper {
            override fun map(field: ObtainFieldName, order: OrderType): Flow<List<SongUi>> {
                repository.sorted(field, order)
                return flowOf()
            }
        }
    }

    private interface FakeSortType : SortType.Map {
        class Base(
            private val field: ObtainFieldName,
            private val type: OrderType
        ) : FakeSortType {
            override fun <T> map(mapper: SortType.Mapper<T>): Flow<List<T>> =
                mapper.map(field, type)
        }
    }
}