package com.example.tagplayer.main.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tagplayer.core.ConsumeState

interface Screen {
    fun dispatch(fragmentManager: FragmentManager, containerId: Int)
    fun consumed(viewModel: ConsumeState) = viewModel.consumeState()

    class Add(
        private val fragmentClass: Class<out Fragment>
    ) : Screen {
        override fun dispatch(
            fragmentManager: FragmentManager,
            containerId: Int
        ) {
            fragmentManager.beginTransaction().add(
                containerId,
                fragmentClass.getDeclaredConstructor().newInstance(),
                fragmentClass.name.toString()
            ).commit()
        }
    }

    object Empty : Screen {
        override fun dispatch(fragmentManager: FragmentManager, containerId: Int) = Unit
    }
}