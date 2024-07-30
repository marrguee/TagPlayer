package com.example.tagplayer.core

interface ModifyCustomImage {

    interface Background {
        fun background(image: CustomImage)
    }

    interface Src {
        fun src(resourceId: Int)
    }

    interface Mutable : Background, Src
}