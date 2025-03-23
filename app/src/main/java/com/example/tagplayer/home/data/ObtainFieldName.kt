package com.example.tagplayer.home.data

import com.example.tagplayer.core.data.database.SortField
import com.example.tagplayer.core.data.database.models.Song
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

interface ObtainFieldName {
    fun name() : String

    abstract class Base(private val field: KProperty1<Song, *>) : ObtainFieldName {
        override fun name() : String = field.javaField?.getAnnotation(SortField::class.java)?.run {
            name.takeIf { it.isNotEmpty() }
        } ?: field.name
    }

    object SongTitle : Base(Song::title)

    object SongDate : Base(Song::dateModified)
}