package com.example.tagplayer.tags_attach.presentation

import com.example.tagplayer.FakeAllHandleStateObservable
import com.example.tagplayer.FakeDispatcherList
import com.example.tagplayer.FakeHandleDeath
import com.example.tagplayer.FakeNavigation
import com.example.tagplayer.FakeRunAsync
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.main.presentation.navigation.Screen
import com.example.tagplayer.tags_attach.domain.AttachTagsInteractor
import com.example.tagplayer.tags_attach.domain.TagsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class AttachTagsViewModelTest {

    class Main {
        private lateinit var viewModel: AttachTagsViewModel

        private lateinit var interactor: FakeAttachTagsInteractor
        private lateinit var navigation: FakeNavigation
        private lateinit var observable: FakeObservable
        private lateinit var handleDeath: FakeHandleDeath
        private lateinit var runAsync: FakeRunAsync
        private lateinit var mapper: FakeMapper

        private lateinit var dispatcherList: FakeDispatcherList

        @Before
        fun setup() {
            interactor = FakeAttachTagsInteractor.Main()
            navigation = FakeNavigation.Base()
            observable = FakeObservable.Base()
            handleDeath = FakeHandleDeath.Base()
            dispatcherList = FakeDispatcherList.Base()
            mapper = FakeMapper.Base(observable = observable)
            runAsync = FakeRunAsync.Base()

            viewModel = AttachTagsViewModel(
                runAsync,
                interactor,
                navigation,
                observable,
                mapper,
                handleDeath,
            )
        }

        @Test
        fun `first start`() {
            viewModel.init(0L)

            handleDeath.checkHandleDeathCalled(1)
            interactor.checkTagsCalled(1)

            runAsync.pingResult()

            observable.checkState(AttachTagsState.DragAndDrop(listOf(), listOf()))

            val observer = object : CustomObserver<AttachTagsState> {
                override fun update(data: AttachTagsState) = data.consumed(viewModel)
            }
            viewModel.startGettingUpdates(observer)
            observable.checkObserver(observer)
            observable.checkClearTimes(0)
        }

        @Test
        fun `configuration changed`() {
            viewModel.init(0L)
            val observer = object : CustomObserver<AttachTagsState> {
                override fun update(data: AttachTagsState) {
                    data.consumed(viewModel)
                }
            }
            viewModel.startGettingUpdates(observer)
            runAsync.pingResult()
            runAsync.checkHandleCalled(1)
            handleDeath.checkHandleDeathCalled(1)
            observable.checkState(AttachTagsState.DragAndDrop(listOf(), listOf()))

            viewModel.stopGettingUpdates()
            observable.checkObserver(AttachTagsObserver.Empty)

            viewModel.startGettingUpdates(observer)
            runAsync.checkHandleCalled(1)
            handleDeath.checkHandleDeathCalled(1)
            observable.checkState(AttachTagsState.DragAndDrop(listOf(), listOf()))
        }

        @Test
        fun `add tag to owned`() {
            viewModel.init(0L)
            val observer = object : CustomObserver<AttachTagsState> {
                override fun update(data: AttachTagsState) {
                    data.consumed(viewModel)
                }
            }
            viewModel.startGettingUpdates(observer)
            runAsync.pingResult()
            runAsync.checkHandleCalled(1)

            viewModel.addToOwned(0L)
            runAsync.pingResult()
            runAsync.checkHandleCalled(2)
            interactor.checkAddCalled(1)
        }

        @Test
        fun `remove tag from owned`() {
            viewModel.init(0L)
            val observer = object : CustomObserver<AttachTagsState> {
                override fun update(data: AttachTagsState) {
                    data.consumed(viewModel)
                }
            }
            viewModel.startGettingUpdates(observer)
            runAsync.pingResult()
            runAsync.checkHandleCalled(1)

            viewModel.removeFromOwned(0L)
            runAsync.pingResult()
            runAsync.checkHandleCalled(2)
            interactor.checkRemoveCalled(1)
        }

        @Test
        fun `handle comeback`() {
            viewModel.comeback()
            navigation.checkScreen(Screen.Pop)
        }
    }

    class Error {
        private lateinit var viewModel: AttachTagsViewModel

        private lateinit var interactor: FakeAttachTagsInteractor
        private lateinit var navigation: FakeNavigation
        private lateinit var observable: FakeObservable
        private lateinit var handleDeath: FakeHandleDeath
        private lateinit var runAsync: FakeRunAsync
        private lateinit var mapper: FakeMapper

        private lateinit var dispatcherList: FakeDispatcherList

        @Before
        fun setup() {
            interactor = FakeAttachTagsInteractor.Error()
            navigation = FakeNavigation.Base()
            observable = FakeObservable.Base()
            handleDeath = FakeHandleDeath.Base()
            dispatcherList = FakeDispatcherList.Base()
            mapper = FakeMapper.Base(observable = observable)
            runAsync = FakeRunAsync.Base()

            viewModel = AttachTagsViewModel(
                runAsync,
                interactor,
                navigation,
                observable,
                mapper,
                handleDeath,
            )
        }

        @Test
        fun `first start`() {
            viewModel.init(0L)
            runAsync.pingResult()

            handleDeath.checkHandleDeathCalled(1)
            interactor.checkTagsCalled(1)
            observable.checkState(AttachTagsState.Error(String()))

            val observer = object : CustomObserver<AttachTagsState> {
                override fun update(data: AttachTagsState) = data.consumed(viewModel)
            }
            viewModel.startGettingUpdates(observer)

            observable.checkObserver(observer)
            observable.checkClearTimes(1)
            observable.checkState(AttachTagsState.Empty)
        }

        @Test
        fun `configuration changed while error throw`() {
            viewModel.init(0L)
            runAsync.pingResult()

            observable.checkState(AttachTagsState.Error(String()))
            observable.checkClearTimes(0)

            val observer = object : CustomObserver<AttachTagsState> {
                override fun update(data: AttachTagsState) = data.consumed(viewModel)
            }
            observable.checkState(AttachTagsState.Error(String()))

            viewModel.startGettingUpdates(observer)

            observable.checkClearTimes(1)
            observable.checkState(AttachTagsState.Empty)
        }

        @Test
        fun `add tag to owned`() {
            viewModel.addToOwned(0L)
            runAsync.pingResult()
            interactor.checkAddCalled(1)
            observable.checkState(AttachTagsState.Error(String()))
        }

        @Test
        fun `remove tag from owned`() {
            viewModel.removeFromOwned(0L)
            runAsync.pingResult()
            interactor.checkRemoveCalled(1)
            observable.checkState(AttachTagsState.Error(String()))
        }
    }

    private interface FakeAttachTagsInteractor : AttachTagsInteractor {
        fun checkTagsCalled(times: Int)
        fun checkAddCalled(times: Int)
        fun checkRemoveCalled(times: Int)

        abstract class Base : FakeAttachTagsInteractor {
            protected var tagsTimes: Int = 0
            protected var addTimes: Int = 0
            protected var removeTimes: Int = 0

            override fun checkTagsCalled(times: Int) = assertEquals(times, tagsTimes)

            override fun checkAddCalled(times: Int) = assertEquals(times, addTimes)

            override fun checkRemoveCalled(times: Int) = assertEquals(times, removeTimes)
        }

        class Main : Base(), FakeAttachTagsInteractor {
            override fun tags(songId: Long): TagsResponse {
                tagsTimes++
                return TagsResponse.Success(
                    all = flowOf<List<TagUi>>(),
                    owned = flowOf<List<TagUi>>()
                )
            }

            override suspend fun add(songId: Long, tagId: Long): TagsResponse {
                addTimes++
                return TagsResponse.Empty
            }

            override suspend fun remove(songId: Long, tagId: Long): TagsResponse {
                removeTimes++
                return TagsResponse.Empty
            }
        }

        class Error : Base(), FakeAttachTagsInteractor {
            override fun tags(songId: Long): TagsResponse {
                tagsTimes++
                return TagsResponse.Error(String())
            }

            override suspend fun add(songId: Long, tagId: Long): TagsResponse {
                addTimes++
                return TagsResponse.Error(String())
            }

            override suspend fun remove(songId: Long, tagId: Long): TagsResponse {
                removeTimes++
                return TagsResponse.Error(String())
            }
        }
    }

    private interface FakeObservable : FakeAllHandleStateObservable<AttachTagsState> {
        class Base : FakeObservable,
            FakeAllHandleStateObservable.Base<AttachTagsState>(
                AttachTagsState.Empty, AttachTagsObserver.Empty
            )
    }

    private interface FakeMapper : TagsResponse.Mapper {
        class Base(
            private val observable: CustomObservable.UpdateUi<AttachTagsState>
        ) : FakeMapper {

            override fun mapSuccess(
                all: Flow<List<TagUi>>,
                owned: Flow<List<TagUi>>,
                scope: CoroutineScope
            ) = observable.update(AttachTagsState.DragAndDrop(listOf(), listOf()))

            override fun mapError(error: String) = observable.update(AttachTagsState.Error(error))
        }
    }
}