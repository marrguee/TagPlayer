package com.example.tagplayer.main.presentation

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val manageTab: ManageTab
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = manageTab.count()
    override fun createFragment(position: Int) = manageTab.manage(position)
}