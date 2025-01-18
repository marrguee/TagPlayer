package com.example.tagplayer.main.presentation

import com.example.tagplayer.tag_settings.presentation.MenuAction

interface PopupMenuReaction {
    fun popup(menuId: Int, action: MenuAction)
}