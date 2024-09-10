package com.example.tagplayer.edit_song_tag

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicLong

class EditSongTagsViewModel(
    private val interactor: EditSongTagInteractor,
    private val navigation: Navigation.Navigate,
    private val communication: Communication<EditSongTagState>
) : ViewModel() {
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
                    communication.update(EditSongTagState.ChangeAllTagsSplashState(true))
                if (ownedTagList.isEmpty())
                    communication.update(EditSongTagState.ChangeOwnedTagsSplashState(true))
                communication.update(
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

            communication.update(EditSongTagState.ChangeAllTagsSplashState(allTagList.isEmpty()))
            communication.update(EditSongTagState.ChangeOwnedTagsSplashState(ownedTagList.isEmpty()))
            communication.update(
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

    fun observe(owner: LifecycleOwner, observer: Observer<in EditSongTagState>) {
        communication.observe(owner, observer)
    }

    companion object {
        private const val DEFAULT_SONG_ID = Long.MIN_VALUE
    }
}