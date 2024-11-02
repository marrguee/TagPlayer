package com.example.tagplayer.main.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tagplayer.core.domain.HandleUiStateUpdates

interface Screen {
    fun dispatch(fragmentManager: FragmentManager, containerId: Int)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

    open class Add(
        private val fragmentClass: Class<out Fragment>
    ) : Screen {
        override fun dispatch(
            fragmentManager: FragmentManager,
            containerId: Int
        ) {
            fragmentManager.beginTransaction()
                .add(
                    containerId,
                    fragmentClass.getDeclaredConstructor().newInstance(),
                    fragmentClass.name.toString()
                ).commit()
        }
    }

    open class AddWithBackstack(
        private val fragmentClass: Class<out Fragment>
    ) : Screen {
        override fun dispatch(
            fragmentManager: FragmentManager,
            containerId: Int
        ) {
            fragmentManager.beginTransaction()
                .add(
                    containerId,
                    fragmentClass.getDeclaredConstructor().newInstance(),
                    fragmentClass.name.toString()
                )
                .addToBackStack(fragmentClass.name.toString())
                .commit()
        }
    }

    class Replace(
        private val fragmentClass: Class<out Fragment>
    ) : Screen {
        override fun dispatch(
            fragmentManager: FragmentManager,
            containerId: Int
        ) {
            fragmentManager.beginTransaction().replace(
                containerId,
                fragmentClass.getDeclaredConstructor().newInstance(),
                fragmentClass.name.toString()
            ).commit()
        }
    }

    open class ReplaceWithBackstack(
        private val fragmentClass: Class<out Fragment>
    ) : Screen {
        override fun dispatch(
            fragmentManager: FragmentManager,
            containerId: Int
        ) {
            fragmentManager.beginTransaction()
                .replace(
                    containerId,
                    fragmentClass.getDeclaredConstructor().newInstance(),
                    fragmentClass.name.toString()
                )
                .addToBackStack(fragmentClass.name.toString())
                .commit()
        }
    }

    object Pop : Screen {
        override fun dispatch(fragmentManager: FragmentManager, containerId: Int) {
            fragmentManager.popBackStack()
        }
    }

    object Empty : Screen {
        override fun dispatch(fragmentManager: FragmentManager, containerId: Int) = Unit
    }
}