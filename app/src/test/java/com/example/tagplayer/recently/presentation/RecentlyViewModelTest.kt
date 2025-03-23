package com.example.tagplayer.recently.presentation

import com.example.tagplayer.FakeAllObservable
import com.example.tagplayer.FakeClearViewModel
import com.example.tagplayer.FakeHandleDeath
import com.example.tagplayer.FakeNavigation
import com.example.tagplayer.FakeRunAsync
import com.example.tagplayer.tags_attach.presentation.AttachTagsScreen
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.main.presentation.navigation.Screen
import com.example.tagplayer.recently.domain.RecentlyInteractor
import com.example.tagplayer.recently.domain.RecentlyResponse
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RecentlyViewModelTest {

    private lateinit var runAsync: FakeRunAsync
    private lateinit var interactor: FakeRecentlyInteractor
    private lateinit var observable: FakeRecentlyObservable
    private lateinit var mapper: FakeRecentlyMapper
    private lateinit var navigation: FakeNavigation
    private lateinit var handleDeath: FakeHandleDeath
    private lateinit var clearViewModel: FakeClearViewModel

    private lateinit var viewModel: RecentlyViewModel

    @Before
    fun setup() {
        runAsync = FakeRunAsync.Base()
        interactor = FakeRecentlyInteractor.Base()
        observable = FakeRecentlyObservable.Base()
        mapper = FakeRecentlyMapper.Base(observable)
        navigation = FakeNavigation.Base()
        handleDeath = FakeHandleDeath.Base()
        clearViewModel = FakeClearViewModel.Base()

        viewModel = RecentlyViewModel(
            runAsync, interactor, observable, mapper, navigation, handleDeath, clearViewModel
        )
    }

    @Test
    fun `test init when death happened`() {
        viewModel.init()
        viewModel.init()
        runAsync.checkHandleCalled(1)
        handleDeath.checkHandleDeathCalled(1)
    }

    @Test
    fun `test play calls interactor`() {
        val songId = 42L
        viewModel.play(songId)
        interactor.checkPlayCalledWithId(songId)
    }

    @Test
    fun `test start and stop getting updates`() {
        val observer = object : CustomObserver<RecentlyState> {
            override fun update(data: RecentlyState) {
                data.consumed(viewModel)
            }
        }
        viewModel.startGettingUpdates(observer)
        observable.checkObserver(observer)

        viewModel.stopGettingUpdates()
        observable.checkObserver(RecentlyObserver.Empty)
    }

    @Test
    fun `test clear`() {
        viewModel.clear()
        observable.checkClearTimes(1)
    }

    @Test
    fun `test attach tags screen`() {
        val songId = 55L
        viewModel.attachTagsScreen(songId)
        navigation.checkScreen(AttachTagsScreen(songId))
    }

    @Test
    fun `test comeback`() {
        viewModel.comeback()
        navigation.checkScreen(Screen.Pop)
    }

    @Test
    fun `test recently success response`() {
        val recentlyUiList = emptyList<RecentlyUi>()
        interactor.setRecentlyResponse(RecentlyResponse.RecentlyResponseSuccess(recentlyUiList))

        viewModel.init()
        runAsync.pingResult()
        runAsync.checkHandleCalled(1)
        interactor.checkRecentlyCalled(1)
        mapper.checkMappedSuccess(recentlyUiList)
        observable.checkState(RecentlyState.RecentlyUpdated(recentlyUiList))
    }

    @Test
    fun `test recently error response`() {
        val errorMsg = "Network Error"
        interactor.setRecentlyResponse(RecentlyResponse.RecentlyResponseError(errorMsg))

        viewModel.init()
        runAsync.pingResult()
        runAsync.checkHandleCalled(1)
        interactor.checkRecentlyCalled(1)
        mapper.checkMappedError(errorMsg)
        observable.checkState(RecentlyState.Error(errorMsg))
    }

    private interface FakeRecentlyInteractor : RecentlyInteractor {
        fun checkRecentlyCalled(times: Int)
        fun setRecentlyResponse(response: RecentlyResponse)
        fun checkPlayCalledWithId(expected: Long)

        class Base : FakeRecentlyInteractor {
            private var recentlyCalled = 0
            private var playCalledWith: Long? = null
            private var response: RecentlyResponse =
                RecentlyResponse.RecentlyResponseSuccess(emptyList())

            override suspend fun recently(): RecentlyResponse {
                recentlyCalled++
                return response
            }

            override fun play(id: Long) {
                playCalledWith = id
            }

            override fun checkRecentlyCalled(times: Int) = assertEquals(times, recentlyCalled)

            override fun setRecentlyResponse(response: RecentlyResponse) {
                this.response = response
            }

            override fun checkPlayCalledWithId(expected: Long) =
                assertEquals(expected, playCalledWith)
        }
    }

    private interface FakeRecentlyMapper : RecentlyResponse.HistoryResponseMapper {
        fun checkMappedSuccess(expected: List<RecentlyUi>)
        fun checkMappedError(expected: String)

        class Base(
            private val observable: FakeRecentlyObservable
        ) : FakeRecentlyMapper {

            private var lastSuccessList: List<RecentlyUi> = emptyList()
            private var lastErrorMsg = String()

            override fun mapSuccess(list: List<RecentlyUi>) {
                lastSuccessList = list
                observable.update(RecentlyState.RecentlyUpdated(list))
            }

            override fun mapError(msg: String) {
                lastErrorMsg = msg
                observable.update(RecentlyState.Error(msg))
            }

            override fun checkMappedSuccess(expected: List<RecentlyUi>) =
                assertEquals(expected, lastSuccessList)

            override fun checkMappedError(expected: String) =
                assertEquals(expected, lastErrorMsg)
        }
    }


    private interface FakeRecentlyObservable : FakeAllObservable<RecentlyState> {
        class Base : FakeRecentlyObservable,
            FakeAllObservable.Base<RecentlyState>(RecentlyState.Empty, RecentlyObserver.Empty)
    }
}

