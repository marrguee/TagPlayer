package com.example.tagplayer.main.presentation.navigation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.presentation.fragments.ArgumentType

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

    @Suppress("UNCHECKED_CAST")
    open class AddWithArgumentsBackstack<A : Any, F : Fragment>(
        private val argument: A,
        private val argumentsTypes: ArgumentType<A>,
        private val fragmentClass: Class<out Fragment>
    ) : Screen {
        override fun dispatch(
            fragmentManager: FragmentManager,
            containerId: Int
        ) {
            val method = fragmentClass.getMethod(
                "instance",
                argumentsTypes.type()
            )

            val fragment = fragmentClass.getConstructor().newInstance()
            val argsFragment = try {
                method.invoke(
                    fragment,
                    argument
                ) as F
            } catch (e: Exception) {
                fragment
            }

            fragmentManager.beginTransaction()
                .add(containerId, argsFragment)
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

    @Suppress("UNCHECKED_CAST")
    open class ShowDialog<T, F : DialogFragment>(
        private val argument: T?,
        private val argumentsTypes: ArgumentType<T>,
        private val fragmentClass: Class<out DialogFragment>
    ) : Screen {
        override fun dispatch(fragmentManager: FragmentManager, containerId: Int) {
            var dialog = fragmentClass.getConstructor().newInstance()
            if (argument != null) {
                dialog = try {
                    fragmentClass.getMethod(
                        "instance",
                        argumentsTypes.type()
                    ).invoke(
                        dialog,
                        argument
                    ) as F
                } catch (e: Exception) {
                    dialog
                }
            }
            dialog.show(fragmentManager, fragmentManager::class.java.name)
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