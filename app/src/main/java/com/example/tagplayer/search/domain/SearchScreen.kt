package com.example.tagplayer.search.domain

import com.example.tagplayer.main.presentation.navigation.Screen
import com.example.tagplayer.search.presentation.SearchFragment

object SearchScreen : Screen.ReplaceWithBackstack(SearchFragment::class.java)