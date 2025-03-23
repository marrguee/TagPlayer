package com.example.tagplayer.filter.data

import com.example.tagplayer.FakeHandleTry
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.data.database.models.SongTag.Mapper
import com.example.tagplayer.filter.domain.FilterDomain
import com.example.tagplayer.filter.domain.FilterRepository
import com.example.tagplayer.filter.domain.errors.FilterException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FilterRepositoryTest {
    private lateinit var repository: FilterRepository<FilterDomain>
    private lateinit var cacheDatasource: FakeCacheDatasource
    private lateinit var handleTry: FakeHandleTry<FilterException>
    private lateinit var mapper: FakeMapper

    @Before
    fun setup() {
        cacheDatasource = FakeCacheDatasource.Base()
        handleTry = FakeHandleTry.Base()
        mapper = FakeMapper.Domain
        repository = FilterRepositoryImpl(handleTry, cacheDatasource, mapper)
    }

    @Test
    fun `check tags`() = runBlocking {
        val result: Flow<List<FilterDomain>> = repository.tags()
        cacheDatasource.checkTagsCalled(1)
    }

    @Test
    fun `save filters`() = runBlocking {
        repository.save(Pair(0L, true))
        cacheDatasource.checkSaveCalled(1)
    }

    @Test
    fun `reset filters`() = runBlocking {
        repository.reset()
        cacheDatasource.checkResetCalled(1)
    }

    private interface FakeCacheDatasource : FilterCacheDatasource {
        fun checkTagsCalled(times: Int)
        fun checkSaveCalled(times: Int)
        fun checkResetCalled(times: Int)

        class Base : FakeCacheDatasource {
            private var tagsCalled = 0
            private var saveCalled = 0
            private var resetCalled = 0

            override fun checkTagsCalled(times: Int) = assertEquals(times, tagsCalled)

            override fun checkSaveCalled(times: Int) = assertEquals(times, saveCalled)

            override fun checkResetCalled(times: Int) = assertEquals(times, resetCalled)

            override fun tags(): Flow<List<SongTag>> {
                tagsCalled++
                return flowOf()
            }

            override suspend fun save(filter: Pair<Long, Boolean>) {
                saveCalled++
            }

            override suspend fun reset() {
                resetCalled++
            }
        }
    }

    private interface FakeMapper : Mapper<FilterDomain> {
        object Domain : FakeMapper {
            override fun map(id: Long, title: String, color: String, selected: Boolean) =
                FilterDomain(id, title, color, selected)
        }
    }
}