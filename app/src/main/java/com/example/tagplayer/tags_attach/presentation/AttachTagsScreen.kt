package com.example.tagplayer.tags_attach.presentation

import com.example.tagplayer.core.presentation.fragments.ArgumentType
import com.example.tagplayer.main.presentation.navigation.Screen

data class AttachTagsScreen(private val arguments: Long) :
    Screen.AddWithArgumentsBackstack<Long, AttachTagsFragment>(
        arguments,
        ArgumentType.LongType,
        AttachTagsFragment::class.java
    )