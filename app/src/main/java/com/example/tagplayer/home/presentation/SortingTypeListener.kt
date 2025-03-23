package com.example.tagplayer.home.presentation

import android.view.View
import android.widget.AdapterView
import com.example.tagplayer.home.domain.SortType

interface SortingTypeListener : AdapterView.OnItemSelectedListener{

    class Base(
        private val itemSelected: SortSongs,
    ) : SortingTypeListener, AdapterView.OnItemSelectedListener {
        private var type: SortType.Map = SortType.Empty
        private val types: List<SortType.All> = listOf(
            SortType.TitleAsc,
            SortType.TitleDesc,
            SortType.DateAsc,
            SortType.DateDesc
        )

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            types.find { it.compare(position) }?.let {
                if (type == it) return
                type = it
                itemSelected.sort(type)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
    }
}