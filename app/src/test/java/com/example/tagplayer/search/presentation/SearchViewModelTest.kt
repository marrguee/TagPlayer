package com.example.tagplayer.search.presentation

import com.example.tagplayer.FakeAllObservable
import com.example.tagplayer.FakeNavigation
import com.example.tagplayer.FakeRunAsync
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.main.presentation.navigation.Screen
import com.example.tagplayer.search.domain.SearchInteractor
import com.example.tagplayer.search.domain.SearchResponse
import com.example.tagplayer.tags_attach.presentation.AttachTagsScreen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {
    private lateinit var viewModel: SearchViewModel

    private lateinit var runAsync: FakeRunAsync
    private lateinit var interactor: FakeInteractor
    private lateinit var observable: FakeObservable
    private lateinit var mapper: FakeMapper
    private lateinit var navigation: FakeNavigation

    @Before
    fun setup() {
        runAsync = FakeRunAsync.Base()
        interactor = FakeInteractor.Base()
        observable = FakeObservable.Base()
        mapper = FakeMapper.Base(observable)
        navigation = FakeNavigation.Base()

        viewModel = SearchViewModel(
            runAsync,
            interactor,
            observable,
            mapper,
            navigation
        )
    }

    @Test
    fun `test search calls interactor`() {
        val query = "test query"
        viewModel.search(query)
        runAsync.pingResult()
        interactor.checkSearchCalledWithQuery(query)
    }

    @Test
    fun `test play calls interactor`() {
        val songId = 42L
        viewModel.play(songId)
        interactor.checkPlayCalledWithId(songId)
    }

    @Test
    fun `test start and stop getting updates`() {
        val observer = object : CustomObserver<SearchState> {
            override fun update(data: SearchState) {}
        }
        viewModel.startGettingUpdates(observer)
        observable.checkObserver(observer)

        viewModel.stopGettingUpdates()
        observable.checkObserver(SearchObserver.Empty)
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
    fun `test search success response`() {
        val searchUiList = emptyList<SearchUi>()
        interactor.setSearchResponse(SearchResponse.SongsSuccess(searchUiList))

        viewModel.search("query")
        runAsync.pingResult()
        runAsync.checkHandleCalled(1)
        interactor.checkSearchCalledWithQuery("query")
        mapper.checkMappedSuccess(searchUiList)
        observable.checkState(SearchState.SongsSuccess(searchUiList))
    }

    @Test
    fun `test search error response`() {
        val errorMsg = "Network Error"
        interactor.setSearchResponse(SearchResponse.Error(errorMsg))

        viewModel.search("query")
        runAsync.pingResult()
        runAsync.checkHandleCalled(1)
        interactor.checkSearchCalledWithQuery("query")
        mapper.checkMappedError(errorMsg)
        observable.checkState(SearchState.Error(errorMsg))
    }

    private interface FakeInteractor : SearchInteractor {
        fun checkPlayCalledWithId(id: Long)
        fun checkSearchCalledWithQuery(query: String)
        fun setSearchResponse(response: SearchResponse)

        class Base : FakeInteractor {
            private var songId = 0L
            private var cacheQuery = String()
            private var searchResponse: SearchResponse = SearchResponse.SongsSuccess(emptyList())

            override fun checkPlayCalledWithId(id: Long) = assertEquals(id, songId)

            override fun checkSearchCalledWithQuery(query: String) = assertEquals(query, cacheQuery)

            override fun setSearchResponse(response: SearchResponse) {
                searchResponse = response
            }

            override fun play(id: Long) {
                songId = id
            }

            override suspend fun search(query: String): SearchResponse {
                cacheQuery = query
                return searchResponse
            }
        }
    }

    private interface FakeObservable : FakeAllObservable<SearchState> {
        class Base : FakeObservable,
            FakeAllObservable.Base<SearchState>(SearchState.Empty, SearchObserver.Empty)
    }

    private interface FakeMapper : SearchResponse.Mapper {
        fun checkMappedSuccess(expected: List<SearchUi>)
        fun checkMappedError(expected: String)

        class Base(private val observable: CustomObservable.UpdateUi<SearchState>) : FakeMapper {
            private var lastSuccessList: List<SearchUi> = emptyList()
            private var lastErrorMsg = String()

            override fun checkMappedSuccess(expected: List<SearchUi>) =
                assertEquals(expected, lastSuccessList)

            override fun checkMappedError(expected: String) =
                assertEquals(expected, lastErrorMsg)

            override fun mapSongsSuccess(list: List<SearchUi>) {
                lastSuccessList = list
                observable.update(SearchState.SongsSuccess(list))
            }

            override fun mapError(cause: String) {
                lastErrorMsg = cause
                observable.update(SearchState.Error(cause))
            }
        }
    }
}