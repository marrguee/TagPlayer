package com.example.tagplayer.home.presentation

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
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

    data class ShowAlertPermissions(
        private val provider: HandleDeclineText
    ) : HomeState {
        override fun dispatch(
            libraryPlaceholder: HideAndShow,
            submitRecently: UpdateList<SongUi>,
            libraryRecycler: UpdateListAndScroll<SongUi>,
            motionLayout: MotionLayout
        ) {
            motionLayout.context.let {
                AlertDialog.Builder(it)
                    .setTitle(
                        ContextCompat.getString(
                            motionLayout.context,
                            R.string.title_permission_dialog
                        )
                    )
                    .setMessage(provider.getDescription())
                    .setPositiveButton(
                        ContextCompat.getString(
                            motionLayout.context,
                            R.string.go_to_settings
                        )
                    ) { _, _ ->
                        it.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .apply {
                                data = Uri.fromParts(
                                    "package",
                                    it.packageName,
                                    null
                                )
                            }
                        )
                    }
                    .setCancelable(false)
                    .show()
            }
        }
    }

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