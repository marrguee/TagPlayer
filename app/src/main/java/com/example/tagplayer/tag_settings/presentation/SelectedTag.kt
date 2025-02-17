package com.example.tagplayer.tag_settings.presentation

interface Selected<T> {
    fun selected() : Boolean
    fun clearSelected()
    fun get(): T
    fun set(data: T)

    object Tag : Selected<TagSettingsUi> {
        private var selected = false
        private var tag: TagSettingsUi? = null

        override fun selected(): Boolean  = synchronized(this) {
            return selected
        }

        override fun clearSelected() = synchronized(this) {
            selected = false
            tag = null
        }

        override fun get(): TagSettingsUi = synchronized(this)  {
            if (!selected) throw IllegalStateException()
            return tag!!
        }

        override fun set(data: TagSettingsUi) = synchronized(this) {
            selected = true
            tag = data
        }
    }
}