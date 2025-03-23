package com.example.tagplayer.core.presentation.custom_views.interfaces

interface ModifyTextView {
    interface Update {
        fun text(text: CharSequence? = null)
    }

    interface Scroll {
        fun scroll(start: Boolean)
    }

    interface Mutable : Update, Scroll
}