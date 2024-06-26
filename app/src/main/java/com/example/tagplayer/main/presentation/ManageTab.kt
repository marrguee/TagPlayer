package com.example.tagplayer.main.presentation

import androidx.fragment.app.Fragment
import com.example.tagplayer.all.presentation.AllFragment
import com.example.tagplayer.history.presentation.HistoryFragment

interface ManageTab {
    fun manage(pos: Int) : Fragment
    fun count() : Int

    object Base : ManageTab {

        override fun manage(pos: Int) = if(pos == 0)
            AllFragment()
        else
            HistoryFragment()

        override fun count() = 2
    }
}