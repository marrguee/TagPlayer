package com.example.tagplayer.home.presentation

import com.example.tagplayer.FakeAllObservable
import com.example.tagplayer.FakeNavigation
import com.example.tagplayer.FakeRunAsync
import com.example.tagplayer.tags_attach.presentation.AttachTagsScreen
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.filter.presentation.FilterTagsScreen
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.SongsResponse
import com.example.tagplayer.home.domain.SortType
import com.example.tagplayer.recently.presentation.RecentlyScreen
import com.example.tagplayer.search.domain.SearchScreen
import com.example.tagplayer.tag_settings.presentation.TagSettingsScreen
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
class HomeViewModelTest {
    class Main {
        private lateinit var viewModel: HomeViewModel
        private lateinit var runAsync: FakeRunAsync
        private lateinit var interactor: FakeHomeInteractor
        private lateinit var observable: FakeObservable
        private lateinit var navigation: FakeNavigation
        private lateinit var mapper: FakeResponseMapper

        @Before
        fun setup() {
            runAsync = FakeRunAsync.Base()
            interactor = FakeHomeInteractor.Base()
            observable = FakeObservable.Base()
            mapper = FakeResponseMapper.Base(observable)
            navigation = FakeNavigation.Base()

            viewModel = HomeViewModel(
                runAsync,
                interactor,
                observable,
                mapper,
                navigation
            )
        }

        @Test
        fun `main scenario`() {
            val observer = object : HomeObserver {
                override fun update(data: HomeState) {
                    data.consumed(viewModel)
                }
            }
            viewModel.startGettingUpdates(observer)

            observable.checkObserver(observer)
            interactor.checkScanCalled(1)

            viewModel.loadRecently()
            runAsync.pingResult()

            runAsync.checkHandleCalled(1)
            observable.checkClearTimes(1)
            interactor.checkRecentlyCalled(1)
            observable.checkState(HomeState.RecentlyUpdated(listOf()))
            observable.runClear()
            observable.checkState(HomeState.Empty)

            viewModel.sort(SortType.Empty)
            runAsync.pingResult()

            runAsync.checkHandleCalled(2)
            interactor.checkSortCalled(1)
            observable.checkClearTimes(2)
            observable.checkState(HomeState.LibraryUpdated(listOf()))
            observable.runClear()
            observable.checkState(HomeState.Empty)
        }

        @Test
        fun `configuration changed`() = runBlocking {
            val observer = object : HomeObserver {
                override fun update(data: HomeState) {
                    data.consumed(viewModel)
                }
            }
            viewModel.startGettingUpdates(observer)

            viewModel.loadRecently()
            runAsync.pingResult()
            observable.runClear()

            viewModel.sort(SortType.Empty)
            runAsync.pingResult()
            observable.runClear()

            viewModel.stopGettingUpdates()

            observable.checkObserver(HomeObserver.Empty)

            viewModel.startGettingUpdates(observer)

            runAsync.checkHandleCalled(2)
            interactor.checkSortCalled(1)
            observable.checkClearTimes(2)
            observable.checkState(HomeState.Empty)
        }

        @Test
        fun `play song foreground`() {
            val songId: Long = 1
            viewModel.play(songId)
            interactor.checkStartPlayWith(songId)
        }

        @Test
        fun `navigation to screen`() {
            viewModel.searchScreen()
            navigation.checkScreen(SearchScreen)

            viewModel.filterTagsScreen()
            navigation.checkScreen(FilterTagsScreen)

            viewModel.recentlyPlayedScreen()
            navigation.checkScreen(RecentlyScreen)

            viewModel.tagSettingsScreen()
            navigation.checkScreen(TagSettingsScreen)

            viewModel.attachTagsScreen(0L)
            navigation.checkScreen(AttachTagsScreen(0L))
        }
    }

    class Error {
        private lateinit var viewModel: HomeViewModel
        private lateinit var runAsync: FakeRunAsync
        private lateinit var interactor: FakeHomeInteractor
        private lateinit var observable: FakeObservable
        private lateinit var navigation: FakeNavigation
        private lateinit var mapper: FakeResponseMapper

        @Before
        fun setup() {
            runAsync = FakeRunAsync.Base()
            interactor = FakeHomeInteractor.Error()
            observable = FakeObservable.Base()
            mapper = FakeResponseMapper.Base(observable)
            navigation = FakeNavigation.Base()

            viewModel = HomeViewModel(
                runAsync,
                interactor,
                observable,
                mapper,
                navigation
            )
        }

        @Test
        fun `main scenario`() {
            val observer = object : HomeObserver {
                override fun update(data: HomeState) {
                    data.consumed(viewModel)
                }
            }
            viewModel.startGettingUpdates(observer)

            observable.checkObserver(observer)
            interactor.checkScanCalled(1)

            viewModel.loadRecently()
            runAsync.pingResult()

            runAsync.checkHandleCalled(1)
            observable.checkClearTimes(1)
            interactor.checkRecentlyCalled(1)
            observable.checkState(HomeState.Error(String()))
            observable.runClear()
            observable.checkState(HomeState.Empty)

            viewModel.sort(SortType.Empty)
            runAsync.pingResult()

            runAsync.checkHandleCalled(2)
            interactor.checkSortCalled(1)
            observable.checkClearTimes(2)
            observable.checkState(HomeState.Error(String()))
            observable.runClear()
            observable.checkState(HomeState.Empty)
        }
    }

    private interface FakeHomeInteractor : HomeInteractor {
        fun checkStartPlayWith(id: Long)
        fun checkScanCalled(times: Int)
        fun checkRecentlyCalled(times: Int)
        fun checkSortCalled(times: Int)

        abstract class Common : FakeHomeInteractor {
            private var songId: Long = Long.MIN_VALUE
            private var scanCalled: Int = 0
            protected var recentlyCalled: Int = 0
            protected var sortCalled: Int = 0

            override fun play(id: Long) {
                songId = id
            }

            override fun checkStartPlayWith(id: Long) {
                assertEquals(id, songId)
            }

            override fun checkScanCalled(times: Int) {
                assertEquals(times, scanCalled)
            }

            override fun checkRecentlyCalled(times: Int) {
                assertEquals(times, recentlyCalled)
            }

            override fun checkSortCalled(times: Int) {
                assertEquals(times, sortCalled)
            }

            override fun scan() {
                scanCalled++
            }
        }

        class Error : Common() {
            override suspend fun croppedRecently(): SongsResponse {
                recentlyCalled++
                return SongsResponse.Error(String())
            }

            override fun sortedSongs(sortingType: SortType.Map): SongsResponse {
                sortCalled++
                return SongsResponse.Error(String())
            }

        }

        class Base : Common() {
            override suspend fun croppedRecently(): SongsResponse {
                recentlyCalled++
                return SongsResponse.Recently(emptyList())
            }

            override fun sortedSongs(sortingType: SortType.Map): SongsResponse {
                sortCalled++
                return SongsResponse.Library(flowOf())
            }
        }
    }

    private interface FakeObservable : FakeAllObservable<HomeState> {
        class Base : FakeObservable,
            FakeAllObservable.Base<HomeState>(HomeState.Empty, HomeObserver.Empty)
    }

    private interface FakeResponseMapper : SongsResponse.Mapper {
        class Base(
            private val observable: CustomObservable.UpdateUi<HomeState>
        ) : FakeResponseMapper {
            override fun mapFlow(flow: Flow<List<SongUi>>, scope: CoroutineScope) {
                observable.update(HomeState.LibraryUpdated(listOf()))
            }

            override fun mapList(list: List<SongUi>) {
                observable.update(HomeState.RecentlyUpdated(listOf()))
            }

            override fun mapError(error: String) {
                observable.update(HomeState.Error(String()))
            }
        }
    }
}