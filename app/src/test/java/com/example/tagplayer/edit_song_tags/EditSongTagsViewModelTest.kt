package com.example.tagplayer.edit_song_tags

import android.os.Bundle
import com.example.tagplayer.FakeClearViewModel
import com.example.tagplayer.FakeDispatcherList
import com.example.tagplayer.FakeHandleDeath
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.HandleSaveRestoreState
import com.example.tagplayer.edit_song_tags.domain.EditSongTagInteractor
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagObserver
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagState
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagsViewModel
import com.example.tagplayer.edit_song_tags.presentation.SaveRestoreEditSongTag
import com.example.tagplayer.edit_song_tags.presentation.TagUi
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EditSongTagsViewModelTest {
    private lateinit var viewModel: EditSongTagsViewModel

    private lateinit var clear: FakeClearViewModel
    private lateinit var dispatcherList: FakeDispatcherList
    private lateinit var interactor: FakeEditSongTagInteractor
    private lateinit var navigation: FakeNavigation
    private lateinit var observable: FakeObservable
    private lateinit var handleDeath: FakeHandleDeath
    private var allTagList: MutableList<TagUi> = mutableListOf()
    private var ownedTagList: MutableList<TagUi> = mutableListOf()
    private var interactorAllTags: List<TagUi> = listOf(
        TagUi(1, "First", "FirstColor"),
        TagUi(2, "Second", "SecondColor")
    )
    private var interactorOwnedTags: List<TagUi> = listOf()

    @Before
    fun setup() {
        clear = FakeClearViewModel.Base()
        dispatcherList = FakeDispatcherList.Base()
        interactor = FakeEditSongTagInteractor.Base(interactorAllTags, interactorOwnedTags)
        navigation = FakeNavigation.Base()
        observable = FakeObservable.Base()
        handleDeath = FakeHandleDeath.Base()

        viewModel = EditSongTagsViewModel(
            clear,
            dispatcherList,
            interactor,
            navigation,
            observable,
            handleDeath,
            TagUi.Mapper.Id,
            allTagList,
            ownedTagList
        )
    }

    @Test
    fun firstStart() = runBlocking {
        val songId: Long = 1
        viewModel.consumeId(songId)

        viewModel.init(SaveRestoreEditSongTag(null))

        handleDeath.checkFirstOpeningCalled(1)
        dispatcherList.checkIoTimesCalled(1)
        dispatcherList.checkUiTimesCalled(1)
        interactor.checkOwnedTagsTimesCalled(1)
        interactor.checkAllTagsTimesCalled(1)

        observable.checkSetStateTimes(3)
        observable.checkState(EditSongTagState.DragAndDrop(interactorAllTags, interactorOwnedTags))
    }

    @Test
    fun configurationChanged() = runBlocking {
        val songId: Long = 1
        viewModel.consumeId(songId)

        viewModel.init(SaveRestoreEditSongTag(null))

        handleDeath.checkFirstOpeningCalled(1)
        dispatcherList.checkIoTimesCalled(1)
        dispatcherList.checkUiTimesCalled(1)
        interactor.checkOwnedTagsTimesCalled(1)
        interactor.checkAllTagsTimesCalled(1)

        val stateChanged = 3
        observable.checkSetStateTimes(stateChanged)
        observable.checkState(EditSongTagState.DragAndDrop(interactorAllTags, interactorOwnedTags))

        viewModel.init(SaveRestoreEditSongTag(Bundle()))

        handleDeath.checkFirstOpeningCalled(1)
        dispatcherList.checkIoTimesCalled(1)
        dispatcherList.checkUiTimesCalled(1)
        interactor.checkOwnedTagsTimesCalled(1)
        interactor.checkAllTagsTimesCalled(1)

        observable.checkSetStateTimes(stateChanged)
        observable.checkState(EditSongTagState.DragAndDrop(interactorAllTags, interactorOwnedTags))
    }

    @Test
    fun processDeath() {
        val songId: Long = 1
        viewModel.consumeId(songId)

        viewModel.init(FakeSaveRestoreEditSongTag.Base(null))

        handleDeath.checkFirstOpeningCalled(1)
        dispatcherList.checkIoTimesCalled(1)
        dispatcherList.checkUiTimesCalled(1)
        interactor.checkOwnedTagsTimesCalled(1)
        interactor.checkAllTagsTimesCalled(1)

        var stateChanged = 3
        observable.checkSetStateTimes(stateChanged)
        observable.checkState(EditSongTagState.DragAndDrop(interactorAllTags, interactorOwnedTags))

        val bundle = FakeSaveRestoreEditSongTag.Base(Bundle())
        viewModel.save(bundle)
        observable.checkSaveInstanceCalledTimes(1)

        setup()

        viewModel.init(bundle)

        handleDeath.checkFirstOpeningCalled(0)
        dispatcherList.checkIoTimesCalled(0)
        dispatcherList.checkUiTimesCalled(0)
        interactor.checkOwnedTagsTimesCalled(0)
        interactor.checkAllTagsTimesCalled(0)

        observable.checkRestoreInstanceCalledTimes(1)
        observable.checkState(EditSongTagState.DragAndDrop(interactorAllTags, interactorOwnedTags))
    }

    @Test
    fun dragAndDrop() = runBlocking {
        val songId: Long = 1
        viewModel.consumeId(songId)

        viewModel.init(FakeSaveRestoreEditSongTag.Base(null))

        handleDeath.checkFirstOpeningCalled(1)
        dispatcherList.checkIoTimesCalled(1)
        dispatcherList.checkUiTimesCalled(1)
        interactor.checkOwnedTagsTimesCalled(1)
        interactor.checkAllTagsTimesCalled(1)

        var stateChanged = 3
        observable.checkSetStateTimes(stateChanged)
        observable.checkState(EditSongTagState.DragAndDrop(interactorAllTags, interactorOwnedTags))

        viewModel.dragAndDrop(true, 1)

        stateChanged+=3
        observable.checkSetStateTimes(stateChanged)
        observable.checkState(EditSongTagState.DragAndDrop(allTagList, ownedTagList))

        viewModel.dragAndDrop(false, 1)

        stateChanged+=3
        observable.checkSetStateTimes(stateChanged)
        observable.checkState(EditSongTagState.DragAndDrop(allTagList, ownedTagList))

    }

    private interface FakeSaveRestoreEditSongTag: HandleSaveRestoreState.All<EditSongTagState> {
        class Base(private val bundle: Bundle?): FakeSaveRestoreEditSongTag {
            private var state: EditSongTagState = EditSongTagState.Empty
            override fun save(data: EditSongTagState) {
                state = data
            }

            override fun restore(): EditSongTagState = state

            override fun empty(): Boolean = bundle == null

        }
    }

    private interface FakeEditSongTagInteractor : EditSongTagInteractor {
        fun checkAllTagsTimesCalled(expected: Int)
        fun checkOwnedTagsTimesCalled(expected: Int)
        fun checkSaveOwnedTags(expectedSongId: Long, expectedOwnedTags: List<TagUi>)

        class Base(
            private val returnedAllTags: List<TagUi>,
            private val returnedOwnedTags: List<TagUi>,
        ) : FakeEditSongTagInteractor {
            private var songId: Long = Long.MIN_VALUE
            private var newOwnedTags: List<TagUi> = listOf()
            private var allTagsCalledTimes: Int = 0
            private var ownedTagsCalledTimes: Int = 0

            override fun checkAllTagsTimesCalled(expected: Int) {
                assertEquals(expected, allTagsCalledTimes)
            }

            override fun checkOwnedTagsTimesCalled(expected: Int) {
                assertEquals(expected, ownedTagsCalledTimes)
            }

            override fun checkSaveOwnedTags(expectedSongId: Long, expectedOwnedTags: List<TagUi>) {
                assertEquals(expectedSongId, songId)
                assertEquals(expectedOwnedTags, newOwnedTags)
            }

            override suspend fun allTags(owned: List<Long>): List<TagUi> {
                allTagsCalledTimes++
                return returnedAllTags
            }

            override suspend fun ownedTags(songId: Long): List<TagUi> {
                ownedTagsCalledTimes++
                return returnedOwnedTags
            }

            override suspend fun saveOwnedTags(songId: Long, ownedTags: List<TagUi>) {
                this.songId = songId
                newOwnedTags = ownedTags
            }
        }
    }

    private interface FakeNavigation : Navigation.Navigate {
        fun checkScreen(expected: Screen)

        class Base : FakeNavigation {
            private var screen: Screen = Screen.Empty

            override fun checkScreen(expected: Screen) {
                assertEquals(expected, screen)
            }

            override fun update(data: Screen) {
                screen = data
            }
        }
    }

    private interface FakeObservable : CustomObservable.AllHandleState<EditSongTagState> {
        fun checkObserver(expected: CustomObserver<EditSongTagState>)
        fun checkState(expected: EditSongTagState)
        fun checkSetStateTimes(expected: Int)
        fun checkClearTimes(expected: Int)
        fun checkSaveInstanceCalledTimes(expected: Int)
        fun checkRestoreInstanceCalledTimes(expected: Int)

        class Base : FakeObservable {
            private var observer: CustomObserver<EditSongTagState> = EditSongTagObserver.Empty
            private var state: EditSongTagState = EditSongTagState.Empty

            private var clearTimes: Int = 0
            private var save: Int = 0
            private var restore: Int = 0
            private var setStateTimes: Int = 0

            override fun checkObserver(expected: CustomObserver<EditSongTagState>) {
                assertEquals(expected, observer)
            }

            override fun checkState(expected: EditSongTagState) {
                assertEquals(expected, state)
            }

            override fun checkSetStateTimes(expected: Int) {
                assertEquals(expected, setStateTimes)
            }

            override fun checkClearTimes(expected: Int) {
                assertEquals(expected, clearTimes)
            }

            override fun checkSaveInstanceCalledTimes(expected: Int) {
                assertEquals(expected, save)
            }

            override fun checkRestoreInstanceCalledTimes(expected: Int) {
                assertEquals(expected, restore)
            }

            override fun updateObserver(newObserver: CustomObserver<EditSongTagState>) {
                observer = newObserver
            }

            override fun update(data: EditSongTagState) {
                setStateTimes++
                state = data
            }

            override fun clear() {
                state = EditSongTagState.Empty
                clearTimes++
            }

            override fun save(bundle: HandleSaveRestoreState.Save<EditSongTagState>) {
                save++
                bundle.save(state)
            }

            override fun restore(bundle: HandleSaveRestoreState.Restore<EditSongTagState>) {
                restore++
                state = bundle.restore()
            }
        }
    }
}