package com.example.tagplayer.edit_song_tags.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.core.HandleSaveRestoreState
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.edit_song_tags.domain.EditSongTagInteractor
import com.example.tagplayer.main.presentation.ComebackViewModel
import com.example.tagplayer.main.presentation.HandleSaveAndRestoreState
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicLong

class EditSongTagsViewModel(
    clear: ClearViewModel,
    private val dispatcherList: DispatcherList,
    private val interactor: EditSongTagInteractor,
    private val navigation: Navigation.Navigate,
    private val observable: CustomObservable.AllHandleState<EditSongTagState>,
    private val handleDeath: HandleDeath,
    private var allTagList: MutableList<TagUi> = mutableListOf(),
    private var ownedTagList: MutableList<TagUi> = mutableListOf(),
) : ComebackViewModel(clear), HandleUiStateUpdates.All<EditSongTagState>,
    HandleSaveAndRestoreState<EditSongTagState> {
    private var songId: Long = Long.MIN_VALUE

    fun consumeId(songId: Long) {
        this.songId = songId
    }

    fun dragAndDrop(fromAllToOwned: Boolean, tagId: Long) {
        val (sourceList, destinationList) =
            if (fromAllToOwned) allTagList to ownedTagList else ownedTagList to allTagList

        val item: TagUi? = sourceList.find { it.compare(tagId) }
        item?.let {
            val index = sourceList.indexOf(item)
            sourceList.removeAt(index)
            destinationList.add(item)

            observable.update(EditSongTagState.ChangeAllTagsSplashState(allTagList.isEmpty()))
            observable.update(EditSongTagState.ChangeOwnedTagsSplashState(ownedTagList.isEmpty()))
            observable.update(
                EditSongTagState.DragAndDrop(
                    allTagList.toList(),
                    ownedTagList.toList()
                )
            )
        }
    }

    fun confirm() {
        viewModelScope.launch(dispatcherList.io()) {
            if (songId != DEFAULT_SONG_ID)
                interactor.saveOwnedTags(songId, ownedTagList)
            withContext(dispatcherList.ui()) {
                navigation.update(Screen.Pop)
            }
        }
    }

    override fun startGettingUpdates(observer: CustomObserver<EditSongTagState>) {
        observable.updateObserver(observer)
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(EditSongTagObserver.Empty)
    }

    override fun clear() {
        observable.clear()
    }

    override fun comeback() {
        super.comeback()
        navigation.update(Screen.Pop)
    }

    companion object {
        private const val DEFAULT_SONG_ID = Long.MIN_VALUE
    }

    override fun init(bundle: HandleSaveRestoreState.Restore<EditSongTagState>) {
        if (bundle.empty()){
            viewModelScope.launch(dispatcherList.io()) {
                ownedTagList = interactor.ownedTags(songId) as MutableList<TagUi>
                allTagList = interactor.allTags().filterNot {
                    ownedTagList.contains(it)
                } as MutableList<TagUi>

                withContext(dispatcherList.ui()) {
                    if (allTagList.isEmpty())
                        observable.update(EditSongTagState.ChangeAllTagsSplashState(true))
                    if (ownedTagList.isEmpty())
                        observable.update(EditSongTagState.ChangeOwnedTagsSplashState(true))
                    observable.update(
                        EditSongTagState.DragAndDrop(
                            allTagList.toList(),
                            ownedTagList.toList()
                        )
                    )
                }
            }
            handleDeath.handleFirstStart()
        } else if (handleDeath.deathHappened()) {
            observable.restore(bundle)
            handleDeath.handleDeath()
        }
    }

    override fun save(bundle: HandleSaveRestoreState.Save<EditSongTagState>) {
        observable.save(bundle)
    }
}