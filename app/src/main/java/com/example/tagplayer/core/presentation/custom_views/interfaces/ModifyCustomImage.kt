package com.example.tagplayer.core.presentation.custom_views.interfaces

import com.example.tagplayer.core.domain.CustomImage

interface ModifyCustomImage {

    interface Background {
        fun background(image: CustomImage)
    }

    interface Src {
        fun src(resourceId: Int)
    }

    interface Enabled {
        fun enabled(enabled: Boolean)
    }

    interface Mutable : Background, Src, Enabled
    interface All: Mutable, HandleAnimationCycle
}