package com.example.tagplayer.main.presentation

import androidx.fragment.app.Fragment
import com.example.tagplayer.all.presentation.HomeFragment
import com.example.tagplayer.recently.presentation.RecentlyFragment

interface ManageTab {
    fun manage(pos: Int) : Fragment
    fun count() : Int

    object Base : ManageTab {

        override fun manage(pos: Int) = if(pos == 0)
            HomeFragment()
        else
            RecentlyFragment()

        override fun count() = 2
    }
}