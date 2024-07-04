package com.example.tagplayer.history.presentation

import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.main.presentation.ItemUi
import com.example.tagplayer.main.presentation.ItemUiType
import com.example.tagplayer.main.presentation.MyView

data class DateUi(
    private val date: String
) : ItemUi {

    override fun bind(vararg views: MyView) {
        views[0].title(date)
    }

    override fun same(other: CompareContent) = other.compare(date)

    override fun compare(otherId: Long) = false

    override fun compare(otherDate: String) = otherDate == date

    override fun type(): ItemUiType = ItemUiType.DateType
}
