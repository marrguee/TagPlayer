package com.example.tagplayer.filter.presentation

import com.example.tagplayer.FakeAllHandleStateObservable
import com.example.tagplayer.FakeClearViewModel
import com.example.tagplayer.FakeDispatcherList
import com.example.tagplayer.FakeHandleDeath
import com.example.tagplayer.FakeNavigation
import com.example.tagplayer.FakeRunAsync
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.filter.domain.FilterInteractor
import com.example.tagplayer.filter.domain.FilterResponse
import com.example.tagplayer.main.presentation.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class FilterViewModelTest {
    class Main {
        private lateinit var viewModel: FilterViewModel

        private lateinit var clear: FakeClearViewModel
        private lateinit var runAsync: FakeRunAsync
        private lateinit var dispatcherList: FakeDispatcherList
        private lateinit var observable: FakeObservable
        private lateinit var interactor: FakeInteractor
        private lateinit var navigation: FakeNavigation
        private lateinit var handleDeath: FakeHandleDeath
        private lateinit var mapper: FakeMapper

        @Before
        fun setup() = runBlocking {
            clear = FakeClearViewModel.Base()
            runAsync = FakeRunAsync.Base()
            dispatcherList = FakeDispatcherList.Base()
            observable = FakeObservable.Base()
            interactor = FakeInteractor.Main()
            mapper = FakeMapper.Base(observable)
            navigation = FakeNavigation.Base()
            handleDeath = FakeHandleDeath.Base()

            viewModel = FilterViewModel(
                clear,
                runAsync,
                observable,
                interactor,
                mapper,
                navigation,
                handleDeath
            )
        }

        @Test
        fun `first start`() = runBlocking {
            val observer = object : CustomObserver<FilterState> {
                override fun update(data: FilterState) {
                    data.consumed(viewModel)
                    observable.checkClearTimes(0)
                }
            }
            viewModel.init()
            handleDeath.checkHandleDeathCalled(1)

            runAsync.checkHandleCalled(1)
            viewModel.startGettingUpdates(observer)

            observable.checkObserver(observer)
            observable.checkState(FilterState.Empty)

            runAsync.pingResult()
            interactor.checkTagsCalled(1)
            observable.checkState(FilterState.Filters(emptyList()))
        }

        @Test
        fun `configuration changed`() = runBlocking {
            val observer = object : CustomObserver<FilterState> {
                override fun update(data: FilterState) {
                    data.consumed(viewModel)
                }
            }
            viewModel.init()
            viewModel.startGettingUpdates(observer)
            runAsync.pingResult()

            observable.checkState(FilterState.Filters(emptyList()))

            viewModel.stopGettingUpdates()
            observable.checkObserver(FilterObserver.Empty)

            viewModel.init()
            viewModel.startGettingUpdates(observer)

            handleDeath.checkHandleDeathCalled(1)
            runAsync.checkHandleCalled(1)
            interactor.checkTagsCalled(1)
            observable.checkState(FilterState.Filters(emptyList()))
        }

        @Test
        fun `handle comeback`() = runBlocking {
            viewModel.comeback()
            clear.checkClearCalledTimes(1)
            clear.checkClearCalledWithClass(FilterViewModel::class.java)
            navigation.checkScreen(Screen.Pop)
        }

        @Test
        fun `clear filters`() = runBlocking {
            viewModel.reset()
            runAsync.pingResult()
            runAsync.checkHandleCalled(1)
            interactor.checkResetCalled(1)
        }

        @Test
        fun `save tag`() {
            viewModel.apply(Pair(0, true))
            runAsync.pingResult()
            runAsync.checkHandleCalled(1)
            interactor.checkSaveCalled(1)
        }
    }

    class Error {
        private lateinit var viewModel: FilterViewModel

        private lateinit var clear: FakeClearViewModel
        private lateinit var runAsync: FakeRunAsync
        private lateinit var dispatcherList: FakeDispatcherList
        private lateinit var observable: FakeObservable
        private lateinit var interactor: FakeInteractor
        private lateinit var navigation: FakeNavigation
        private lateinit var handleDeath: FakeHandleDeath
        private lateinit var mapper: FakeMapper

        @Before
        fun setup() = runBlocking {
            clear = FakeClearViewModel.Base()
            runAsync = FakeRunAsync.Base()
            dispatcherList = FakeDispatcherList.Base()
            observable = FakeObservable.Base()
            interactor = FakeInteractor.Error()
            mapper = FakeMapper.Base(observable)
            navigation = FakeNavigation.Base()
            handleDeath = FakeHandleDeath.Base()

            viewModel = FilterViewModel(
                clear,
                runAsync,
                observable,
                interactor,
                mapper,
                navigation,
                handleDeath
            )
        }

        @Test
        fun `configuration changed while error throw`() {
            viewModel.init()

            viewModel.startGettingUpdates(FilterObserver.Empty)

            observable.checkObserver(FilterObserver.Empty)

            runAsync.pingResult()

            observable.checkState(FilterState.Error(String()))

            viewModel.stopGettingUpdates()
            observable.checkClearTimes(0)

            val observer = object : CustomObserver<FilterState> {
                override fun update(data: FilterState) = data.consumed(viewModel)
            }

            viewModel.startGettingUpdates(observer)
            observable.checkClearTimes(1)
            observable.checkState(FilterState.Empty)
        }
    }

    private interface FakeObservable: FakeAllHandleStateObservable<FilterState> {
        class Base : FakeObservable, FakeAllHandleStateObservable.Base<FilterState>(
            FilterState.Empty, FilterObserver.Empty
        )
    }

    private interface FakeInteractor: FilterInteractor {
        fun checkTagsCalled(times: Int)
        fun checkSaveCalled(times: Int)
        fun checkResetCalled(times: Int)

        abstract class Base : FakeInteractor {
            protected var tagsCalled = 0
            protected var saveCalled = 0
            protected var resetCalled = 0

            override fun checkTagsCalled(times: Int) = assertEquals(times, tagsCalled)

            override fun checkSaveCalled(times: Int) = assertEquals(times, saveCalled)

            override fun checkResetCalled(times: Int) = assertEquals(times, resetCalled)
        }

        class Main : Base() {
            override suspend fun tags(): FilterResponse {
                tagsCalled++
                return FilterResponse.Success(flowOf())
            }

            override suspend fun save(filter: Pair<Long, Boolean>): FilterResponse {
                saveCalled++
                return FilterResponse.Empty
            }

            override suspend fun reset(): FilterResponse {
                resetCalled++
                return FilterResponse.Empty
            }
        }

        class Error : Base() {
            override suspend fun tags(): FilterResponse {
                tagsCalled++
                return FilterResponse.Error(String())
            }

            override suspend fun save(filter: Pair<Long, Boolean>): FilterResponse {
                saveCalled++
                return FilterResponse.Error(String())
            }

            override suspend fun reset(): FilterResponse {
                resetCalled++
                return FilterResponse.Error(String())
            }
        }
    }

    private interface FakeMapper : FilterResponse.Mapper {
        class Base(
            private val observable: CustomObservable.UpdateUi<FilterState>
        ) : FakeMapper {

            override fun mapSuccess(flow: Flow<List<FilterUi>>, scope: CoroutineScope) =
                observable.update(FilterState.Filters(emptyList()))

            override fun mapError(error: String) = observable.update(FilterState.Error(error))
        }
    }
}