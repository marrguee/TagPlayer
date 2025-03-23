package com.example.tagplayer.tag_details.presentation

import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.tag_details.presentation.radio_grid.SelectColor
import com.google.android.material.textfield.TextInputEditText

interface TagDialogState {
    fun dispatch(
        dialogTitle: TextView,
        editText: TextInputEditText,
        button: Button,
        selectColor: SelectColor
    )

    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

    data class EditMode(
        private val title: String,
        private val color: String,
    ) : TagDialogState {

        override fun dispatch(
            dialogTitle: TextView,
            editText: TextInputEditText,
            button: Button,
            selectColor: SelectColor
        ) {
            editText.setText(title)
            selectColor.select(color)
            dialogTitle.setText(R.string.edit_tag_title)
            button.setText(R.string.edit_tag_button)
        }
    }

    object AddMode : TagDialogState {
        override fun dispatch(
            dialogTitle: TextView,
            editText: TextInputEditText,
            button: Button,
            selectColor: SelectColor
        ) {
            dialogTitle.setText(R.string.add_tag_title)
            button.setText(R.string.create_tag_button)
        }
    }

    data class Error(private val error: String) : TagDialogState {
        override fun dispatch(
            dialogTitle: TextView,
            editText: TextInputEditText,
            button: Button,
            selectColor: SelectColor
        ) = Toast.makeText(dialogTitle.context, error, Toast.LENGTH_SHORT).show()
    }

    object Empty : TagDialogState {
        override fun dispatch(
            dialogTitle: TextView,
            editText: TextInputEditText,
            button: Button,
            selectColor: SelectColor
        ) = Unit

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }
}