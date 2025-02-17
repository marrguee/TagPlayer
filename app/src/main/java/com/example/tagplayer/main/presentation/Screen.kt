package com.example.tagplayer.main.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tagplayer.core.domain.HandleUiStateUpdates

interface Screen {
    fun dispatch(fragmentManager: FragmentManager, containerId: Int)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

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

    open class AddWithArgumentsBackstack(
        private val arguments: List<Any>,
        private val fragmentClass: Class<out ArgumentsComebackFragment<*, *>>
    ) : Screen {
        override fun dispatch(
            fragmentManager: FragmentManager,
            containerId: Int
        ) {
            val fragment  = fragmentClass.getDeclaredConstructor().newInstance()
            val argsBundle = Bundle().apply {
                putLong(fragment.provideKeyList()[0], arguments[0] as Long)
            }
            fragmentManager.beginTransaction()
                .add(
                    containerId,
                    fragmentClass,
                    argsBundle,
                    fragmentClass.name.toString(),
                )
                .addToBackStack(fragmentClass.name.toString())
                .commit()
        }
    }

    open class Replace(
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