package com.example.tagplayer.main.presentation

import com.example.tagplayer.tagsettings.presentation.MenuAction

interface PopupMenuReaction {
    fun popup(menuId: Int, action: MenuAction)
}