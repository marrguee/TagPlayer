package com.example.tagplayer.filter_by_tags

import android.content.SharedPreferences

interface SharedPrefs {

    interface Save<T> {
        fun save(list: T)
    }

    interface Read<T> {
        fun read() : T
    }

    interface Mutable<T> : Save<T>, Read<T>

    class TagFilterSharedPref(
        private val prefs: SharedPreferences,
        private val key: String = "FilterSharedPrefKey"
    ) : Mutable<List<Long>> {

        override fun save(list: List<Long>) {
            val stringSet = list.map { it.toString() }.toSet()
            prefs.edit().putStringSet(key, stringSet).apply()
        }

        override fun read(): List<Long> {
            val stringSet = prefs.getStringSet(key, emptySet()) ?: return emptyList()
            return stringSet.map { it.toLong() }
        }
    }
}