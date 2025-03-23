package com.example.tagplayer.tag_details.presentation

import com.example.tagplayer.core.presentation.fragments.ArgumentType
import com.example.tagplayer.main.presentation.navigation.Screen

data class TagDetailsScreen(private val id: Long?) : Screen.ShowDialog<Long, TagDetailsFragment>(
    id,
    ArgumentType.LongType,
    TagDetailsFragment::class.java
)