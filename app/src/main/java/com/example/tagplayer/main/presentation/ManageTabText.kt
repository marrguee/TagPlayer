package com.example.tagplayer.main.presentation

import android.content.Context
import com.example.tagplayer.R

class ManageTabText(
    private val context: Context
) : (Int) -> String {

    override fun invoke(pos: Int) = context.getString(
        if (pos == 0)
            R.string.first_tab_name
        else
            R.string.second_tab_name
    )
}