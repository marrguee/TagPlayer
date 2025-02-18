package com.example.tagplayer.core.presentation.fragments

import androidx.fragment.app.Fragment

interface NewInstance<T, E : Fragment> {
    fun instance(args: T): E
}