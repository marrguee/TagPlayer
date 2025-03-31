package com.example.tagplayer.tag_details.presentation

import com.example.tagplayer.FakeAllObservable
import com.example.tagplayer.FakeClearViewModel
import com.example.tagplayer.FakeHandleDeath
import com.example.tagplayer.FakeManageResources
import com.example.tagplayer.FakeNavigation
import com.example.tagplayer.FakeRunAsync
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.presentation.viewmodel.HandleComeback
import com.example.tagplayer.main.presentation.navigation.Screen
import com.example.tagplayer.tag_details.domain.TagDetailsInteractor
import com.example.tagplayer.tag_details.domain.TagDetailsResponse
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class TagViewModelTest {


    class AddTagViewModelTest {
        private lateinit var viewModel: AddTagViewModel

        private lateinit var clear: FakeClearViewModel
        private lateinit var handleDeath: FakeHandleDeath
        private lateinit var runAsync: FakeRunAsync
        private lateinit var interactor: FakeInteractor
        private lateinit var observable: FakeObservable

        @Before
        fun setup() {
            clear = FakeClearViewModel.Base()
            handleDeath = FakeHandleDeath.Base()
            runAsync = FakeRunAsync.Base()
            interactor = FakeInteractor.Base(TagDetailsResponse.Success(String(), String()))
            observable = FakeObservable.Base()

            viewModel = AddTagViewModel(
                clear,
                handleDeath,
                runAsync,
                interactor,
                observable
            )
        }

        @Test
        fun `main scenario`() {
            viewModel.init()
            handleDeath.checkHandleDeathCalled(1)

            val observer = object : CustomObserver<TagDialogState> {
                override fun update(data: TagDialogState) {
                    data.consumed(viewModel)
                }
            }
            observable.checkState(TagDialogState.AddMode)

            viewModel.startGettingUpdates(observer)
            observable.checkObserver(observer)
            observable.checkClearTimes(1)

            observable.checkState(TagDialogState.Empty)


            val dismiss = FakeDismiss.Base(viewModel)
            viewModel.accept("text", "col", dismiss)
            runAsync.pingResult()

            runAsync.checkHandleCalled(1)
            interactor.checkAddCalled(1)
            dismiss.checkDismissCalled(1)
        }

        @Test
        fun `configuration changed`() {
            viewModel.init()

            val observer = object : CustomObserver<TagDialogState> {
                override fun update(data: TagDialogState) = data.consumed(viewModel)
            }
            observable.checkState(TagDialogState.AddMode)

            viewModel.startGettingUpdates(observer)
            viewModel.stopGettingUpdates()

            observable.checkState(TagDialogState.Empty)

            viewModel.startGettingUpdates(observer)

            observable.checkState(TagDialogState.Empty)
        }

        @Test
        fun `process death`() {
            viewModel.init()

            val observer = object : CustomObserver<TagDialogState> {
                override fun update(data: TagDialogState) = data.consumed(viewModel)
            }
            viewModel.startGettingUpdates(observer)
            viewModel.stopGettingUpdates()

            setup()
            viewModel.init()
            handleDeath.checkHandleDeathCalled(1)
            observable.checkState(TagDialogState.AddMode)

            viewModel.startGettingUpdates(observer)
            observable.checkState(TagDialogState.Empty)
        }
    }

    class EditTagViewModelTest {
        private lateinit var viewModel: EditTagViewModel

        private lateinit var clear: FakeClearViewModel
        private lateinit var handleDeath: FakeHandleDeath
        private lateinit var navigation: FakeNavigation
        private lateinit var runAsync: FakeRunAsync
        private lateinit var interactor: FakeInteractor
        private lateinit var observable: FakeObservable

        private lateinit var mapper: FakeMapper
        private lateinit var manageResources: FakeManageResources

        private val tagData: Pair<String, String> = Pair("title", "color")

        @Before
        fun setup() {
            clear = FakeClearViewModel.Base()
            handleDeath = FakeHandleDeath.Base()
            navigation = FakeNavigation.Base()
            runAsync = FakeRunAsync.Base()
            interactor = FakeInteractor.Base(
                TagDetailsResponse.Success(tagData.first, tagData.second)
            )
            observable = FakeObservable.Base()
            mapper = FakeMapper.Base(observable)
            manageResources = FakeManageResources.Base()

            viewModel = EditTagViewModel(
                clear,
                handleDeath,
                observable,
                runAsync,
                interactor,
                mapper,
                manageResources
            )
        }

        @Test
        fun `main scenario`() {
            val tagId = 10L

            viewModel.init(tagId)
            handleDeath.checkHandleDeathCalled(1)
            runAsync.checkHandleCalled(1)
            runAsync.pingResult()
            interactor.checkTagCalledWithId(tagId)
            mapper.checkMapSuccessCalledWith(tagData.first, tagData.second)
            observable.checkState(TagDialogState.EditMode(tagData.first, tagData.second))

            val observer = object : CustomObserver<TagDialogState> {
                override fun update(data: TagDialogState) = data.consumed(viewModel)
            }
            viewModel.startGettingUpdates(observer)

            observable.checkObserver(observer)
            observable.checkClearTimes(1)
            observable.checkState(TagDialogState.Empty)

            val dismiss = FakeDismiss.Base(viewModel)
            viewModel.accept(tagData.first, tagData.second, dismiss)
            runAsync.pingResult()

            runAsync.checkHandleCalled(2)
            interactor.checkAddCalled(1)
            dismiss.checkDismissCalled(1)
            navigation.checkScreen(Screen.Pop)
        }

        @Test
        fun `configuration changed`() {
            val tagId = 10L
            viewModel.init(tagId)
            runAsync.pingResult()
            val observer = object : CustomObserver<TagDialogState> {
                override fun update(data: TagDialogState) = data.consumed(viewModel)
            }
            observable.checkState(TagDialogState.EditMode(tagData.first, tagData.second))
            viewModel.startGettingUpdates(observer)
            viewModel.stopGettingUpdates()

            observable.checkState(TagDialogState.Empty)

            viewModel.startGettingUpdates(observer)

            observable.checkState(TagDialogState.Empty)
        }

        @Test
        fun `process death`() {
            val tagId = 10L
            viewModel.init(tagId)

            val observer = object : CustomObserver<TagDialogState> {
                override fun update(data: TagDialogState) = data.consumed(viewModel)
            }
            viewModel.startGettingUpdates(observer)
            viewModel.stopGettingUpdates()

            setup()
            viewModel.init(tagId)
            handleDeath.checkHandleDeathCalled(1)
            runAsync.checkHandleCalled(1)
            runAsync.pingResult()
            observable.checkState(TagDialogState.EditMode(tagData.first, tagData.second))

            viewModel.startGettingUpdates(observer)

            observable.checkState(TagDialogState.Empty)
        }

        @Test
        fun `tag id don't passed`() {
            viewModel.init()
            observable.checkState(TagDialogState.Error(manageResources.retrieveIdError()))
            clear.checkClearCalledTimes(1)
            navigation.checkScreen(Screen.Pop)
        }
    }

    private interface FakeDismiss : () -> Unit {
        fun checkDismissCalled(tines: Int)

        class Base(private val comeback: HandleComeback) : FakeDismiss {
            private var called = 0

            override fun checkDismissCalled(tines: Int) = assertEquals(tines, called)

            override fun invoke() {
                called++
                comeback.comeback()
            }
        }
    }

    private interface FakeInteractor : TagDetailsInteractor {
        fun checkTagCalledWithId(id: Long)
        fun checkAddCalled(times: Int)

        class Base(
            private val successResponse: TagDetailsResponse.Success
        ) : FakeInteractor {
            private var tagId = 0L
            private var addCalled = 0

            override fun checkTagCalledWithId(id: Long) = assertEquals(id, tagId)

            override fun checkAddCalled(times: Int) = assertEquals(times, addCalled)

            override suspend fun tag(id: Long): TagDetailsResponse {
                tagId = id
                return successResponse
            }

            override suspend fun add(title: String, color: String, id: Long): TagDetailsResponse {
                addCalled++
                return TagDetailsResponse.Empty
            }
        }
    }

    private interface FakeObservable : FakeAllObservable<TagDialogState> {
        class Base : FakeObservable, FakeAllObservable.Base<TagDialogState>(
            TagDialogState.Empty, TagDetailsObserver.Empty
        )
    }

    private interface FakeMapper : TagDetailsResponse.Mapper {
        fun checkMapSuccessCalledWith(title: String, color: String)
        fun checkMapErrorCalledWith(error: String)

        class Base(
            private val observable: CustomObservable.UpdateUi<TagDialogState>
        ) : FakeMapper {
            private var tagData: Pair<String, String>? = null
            private var msg: String? = null

            override fun checkMapSuccessCalledWith(title: String, color: String) =
                assertEquals(Pair(title, color), tagData)


            override fun checkMapErrorCalledWith(error: String) = assertEquals(error, msg)

            override fun mapSuccess(title: String, color: String) {
                tagData = Pair(title, color)
                observable.update(TagDialogState.EditMode(title, color))
            }

            override fun mapError(error: String) {
                msg = error
                observable.update(TagDialogState.Error(error))
            }
        }
    }
}