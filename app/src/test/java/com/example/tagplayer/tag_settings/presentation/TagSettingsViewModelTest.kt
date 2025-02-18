package com.example.tagplayer.tag_settings.presentation

import com.example.tagplayer.FakeAllObservable
import com.example.tagplayer.FakeClearViewModel
import com.example.tagplayer.FakeNavigation
import com.example.tagplayer.FakeRunAsync
import com.example.tagplayer.tag_details.presentation.TagDetailsScreen
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.main.presentation.navigation.Screen
import com.example.tagplayer.tag_settings.domain.TagSettingsInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TagSettingsViewModelTest {
    private lateinit var viewModel: TagSettingsViewModel

    private lateinit var clear: FakeClearViewModel
    private lateinit var runAsync: FakeRunAsync
    private lateinit var interactor: FakeInteractor
    private lateinit var observable: FakeObservable
    private lateinit var mapper: FakeMapper
    private lateinit var navigation: FakeNavigation

    @Before
    fun setup() {
        clear = FakeClearViewModel.Base()
        runAsync = FakeRunAsync.Base()
        interactor = FakeInteractor.Base()
        observable = FakeObservable.Base()
        mapper = FakeMapper.Base(observable)
        navigation = FakeNavigation.Base()

        viewModel = TagSettingsViewModel(
            clear,
            runAsync,
            interactor,
            observable,
            mapper,
            navigation
        )
    }

    @Test
    fun `test loadTags`() {
        viewModel.loadTags()
        interactor.checkTagsCalled(1)
        mapper.checkMapSuccess(1)
    }

    @Test
    fun `test deleteTag`() {
        val tagId = 123L
        viewModel.deleteTag(tagId)
        interactor.checkRemoveCalled(1)
    }

    @Test
    fun `test showTagDialog with id`() {
        val tagId = 456L
        viewModel.showTagDialog(tagId)
        navigation.checkScreen(TagDetailsScreen(tagId))
    }

    @Test
    fun `test showTagDialog without id`() {
        viewModel.showTagDialog()
        navigation.checkScreen(TagDetailsScreen(null))
    }

    @Test
    fun `test comeback`() {
        viewModel.comeback()
        navigation.checkScreen(Screen.Pop)
    }

    private interface FakeInteractor : TagSettingsInteractor {
        fun checkTagsCalled(times: Int)
        fun checkRemoveCalled(times: Int)

        class Base : FakeInteractor {
            private var tagsCalled = 0
            private var removeCalled = 0

            override fun checkTagsCalled(times: Int) = assertEquals(times, tagsCalled)

            override fun checkRemoveCalled(times: Int) = assertEquals(times, removeCalled)

            override fun tags(): TagSettingsResponse {
                tagsCalled++
                return TagSettingsResponse.Success(emptyFlow())
            }

            override suspend fun remove(id: Long): TagSettingsResponse {
                removeCalled++
                return TagSettingsResponse.Empty
            }
        }
    }

    private interface FakeObservable : FakeAllObservable<TagSettingsState> {
        class Base : FakeObservable,
            FakeAllObservable.Base<TagSettingsState>(
                TagSettingsState.Empty, TagSettingsObserver.Empty
            )
    }

    private interface FakeMapper : TagSettingsResponse.Mapper {
        fun checkMapSuccess(times: Int)
        fun checkMapError(times: Int)

        class Base(
            private val observable: CustomObservable.UpdateUi<TagSettingsState>
        ) : FakeMapper {
            private var successCount = 0
            private var errorCount = 0

            override fun checkMapSuccess(times: Int) = assertEquals(times, successCount)

            override fun checkMapError(times: Int) = assertEquals(times, errorCount)

            override fun mapSuccess(
                flow: Flow<List<TagSettingsUi>>,
                coroutineScope: CoroutineScope
            ) {
                successCount++
                observable.update(TagSettingsState.UpdateTags(emptyList()))
            }

            override fun mapError(error: String) {
                errorCount++
                observable.update(TagSettingsState.Error(error))
            }
        }
    }
}