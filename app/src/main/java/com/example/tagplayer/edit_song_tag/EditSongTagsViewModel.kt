package com.example.tagplayer.edit_song_tag

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.main.presentation.ComebackViewModel
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicLong

class EditSongTagsViewModel(
    private val interactor: EditSongTagInteractor,
    private val navigation: Navigation.Navigate,
    private val observable: CustomObservable.All<EditSongTagState>,
    clear: ClearViewModel
) : ComebackViewModel(clear), HandleUiStateUpdates.All<EditSongTagState> {
    private var allTagList: MutableList<TagUi> = mutableListOf()
    private var ownedTagList: MutableList<TagUi> = mutableListOf()
    private var savedSongId: AtomicLong = AtomicLong(DEFAULT_SONG_ID)

    fun init(songId: Long?) {
        if (songId == null) return
        savedSongId.set(songId)
        viewModelScope.launch {
            ownedTagList = interactor.ownedTags(songId) as MutableList<TagUi>
            allTagList = interactor.allTags().filterNot {
                ownedTagList.contains(it)
            } as MutableList<TagUi>

            withContext(Dispatchers.Main.immediate) {
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
        viewModelScope.launch(Dispatchers.IO) {
            if (savedSongId.get() != DEFAULT_SONG_ID)
                interactor.saveOwnedTags(savedSongId.get(), ownedTagList)
            withContext(Dispatchers.Main.immediate) {
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
}