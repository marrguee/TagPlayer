package com.example.tagplayer.tag_settings.add_tag.presentation

import android.os.Parcelable
import android.widget.Button
import android.widget.TextView
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid.SelectColor
import com.example.tagplayer.tag_settings.presentation.TagSettingsUi
import com.google.android.material.textfield.TextInputEditText
import kotlinx.parcelize.Parcelize

interface TagDialogState : Parcelable {
    fun dispatch(
        dialogTitle: TextView,
        editText: TextInputEditText,
        button: Button,
        selectColor: SelectColor
    )

    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

    @Parcelize
    class EditMode(
        private val tagSettingsUi: TagSettingsUi
    ) : TagDialogState {

        override fun dispatch(
            dialogTitle: TextView,
            editText: TextInputEditText,
            button: Button,
            selectColor: SelectColor
        ) {
            tagSettingsUi.fillFields(editText, selectColor)
            dialogTitle.text = dialogTitle.context.resources.getText(R.string.edit_tag_title)
            button.text = button.resources.getText(R.string.edit_tag_button)
        }

    }

    @Parcelize
    object AddMode : TagDialogState {
        override fun dispatch(
            dialogTitle: TextView,
            editText: TextInputEditText,
            button: Button,
            selectColor: SelectColor
        ) {
            dialogTitle.text = dialogTitle.context.resources.getText(R.string.add_tag_title)
            button.text = button.resources.getText(R.string.create_tag_button)
        }
    }

    @Parcelize
    object Empty : TagDialogState {
        override fun dispatch(
            dialogTitle: TextView,
            editText: TextInputEditText,
            button: Button,
            selectColor: SelectColor
        ) =
            Unit

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }
}