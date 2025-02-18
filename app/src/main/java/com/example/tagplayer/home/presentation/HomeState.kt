package com.example.tagplayer.home.presentation

import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.presentation.custom_views.interfaces.HideAndShow
import com.example.tagplayer.core.presentation.custom_views.interfaces.UpdateList
import com.example.tagplayer.core.presentation.custom_views.interfaces.UpdateListAndScroll

interface HomeState {
    fun dispatch(
        libraryPlaceholder: HideAndShow,
        submitRecently: UpdateList<SongUi>,
        libraryRecycler: UpdateListAndScroll<SongUi>,
        motionLayout: MotionLayout,
    )
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

    data class LibraryUpdated(
        private val list: List<SongUi>,
    ) : HomeState {
        override fun dispatch(
            libraryPlaceholder: HideAndShow,
            submitRecently: UpdateList<SongUi>,
            libraryRecycler: UpdateListAndScroll<SongUi>,
            motionLayout: MotionLayout,
        ) {
            libraryRecycler.updateAndScrollToFirst(list)
            libraryPlaceholder.run {
                if (list.isEmpty()) show() else hide()
            }
        }
    }

    data class RecentlyUpdated(
        private val list: List<SongUi>,
    ) : HomeState {
        override fun dispatch(
            libraryPlaceholder: HideAndShow,
            submitRecently: UpdateList<SongUi>,
            libraryRecycler: UpdateListAndScroll<SongUi>,
            motionLayout: MotionLayout,
        ) {
            submitRecently.update(list) {
                motionLayout.enableTransition(R.id.recentlySwipeTransition,
                    if (list.isEmpty()) {
                        if (motionLayout.getTransition(R.id.recentlySwipeTransition).isEnabled)
                            motionLayout.transitionToStart()
                        false
                    } else {
                        if (libraryRecycler.firstItemVisible())
                            motionLayout.transitionToEnd()
                        true
                    }
                )
            }
        }
    }

    data class Error(private val error: String) : HomeState {
        override fun dispatch(
            libraryPlaceholder: HideAndShow,
            submitRecently: UpdateList<SongUi>,
            libraryRecycler: UpdateListAndScroll<SongUi>,
            motionLayout: MotionLayout,
        ) = Toast.makeText(motionLayout.context, error, Toast.LENGTH_SHORT).show()
    }

    object Empty : HomeState {
        override fun dispatch(
            libraryPlaceholder: HideAndShow,
            submitRecently: UpdateList<SongUi>,
            libraryRecycler: UpdateListAndScroll<SongUi>,
            motionLayout: MotionLayout,
        ) = Unit

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }
}