package com.example.tagplayer.tagsettings.presentation

import android.view.MenuItem

class MenuCurator(
    private val menuOptions: List<Pair<Int, MenuAction>>
) {
    fun handleMenuItemClick(item: MenuItem) {
        menuOptions.first { item.itemId == it.first }.second.action()
    }
}

interface MenuAction {
    fun action(vararg args: Any)
}
