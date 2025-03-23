package com.example.tagplayer.filter.domain

import com.example.tagplayer.FakeHandleResponse
import com.example.tagplayer.filter.domain.FilterDomain.Mapper
import com.example.tagplayer.filter.presentation.FilterUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FilterInteractorTest {
    private lateinit var interactor: FilterInteractor
    private lateinit var repository: FakeRepository
    private lateinit var handleResponse: FakeHandleResponse.All<FilterResponse>
    private lateinit var mapper: FakeMapper

    @Before
    fun setup() {
        repository = FakeRepository.Base()
        handleResponse = FakeHandleResponse.Empty(FilterResponse.Empty)
        mapper = FakeMapper.Base
        interactor = FilterInteractor.Base(repository, mapper, handleResponse)
    }

    @Test
    fun `check tags`() = runBlocking {
        val result: FilterResponse = interactor.tags()
        repository.checkTagsCalled(1)
        handleResponse.checkHandleAsyncCalled(1)
    }

    @Test
    fun `save filters`() = runBlocking {
        interactor.save(Pair(0L, true))
        repository.checkSaveCalled(1)
        handleResponse.checkHandleAsyncEmptyCalled(1)
    }

    @Test
    fun `reset filters`() = runBlocking {
        interactor.reset()
        repository.checkResetCalled(1)
        handleResponse.checkHandleAsyncEmptyCalled(1)
    }

    private interface FakeRepository: FilterRepository<FilterDomain> {
        fun checkTagsCalled(times: Int)
        fun checkSaveCalled(times: Int)
        fun checkResetCalled(times: Int)

        class Base : FakeRepository {
            private var tagsCalled = 0
            private var saveCalled = 0
            private var resetCalled = 0

            override fun checkTagsCalled(times: Int) = assertEquals(times, tagsCalled)

            override fun checkSaveCalled(times: Int) = assertEquals(times, saveCalled)

            override fun checkResetCalled(times: Int) = assertEquals(times, resetCalled)

            override fun tags(): Flow<List<FilterDomain>> {
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

    private interface FakeMapper : Mapper<FilterUi> {
        object Base : FakeMapper {
            override fun map(id: Long, title: String, color: String, selected: Boolean): FilterUi =
                FilterUi(id, title, color, selected)
        }
    }
}