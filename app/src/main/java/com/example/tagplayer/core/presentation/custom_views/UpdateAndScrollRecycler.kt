package com.example.tagplayer.core.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.home.presentation.HomeAdapter
import com.example.tagplayer.home.presentation.SongUi
import com.example.tagplayer.core.presentation.custom_views.interfaces.UpdateListAndScroll

class UpdateAndScrollRecycler@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), UpdateListAndScroll<SongUi> {

    override fun updateAndScrollToFirst(list: List<SongUi>) {
        val layoutManager = (layoutManager as LinearLayoutManager)
        val scrollPosition = layoutManager.findFirstVisibleItemPosition()
        (adapter as? HomeAdapter)?.submitList(list) {
            if (scrollPosition != NO_POSITION) {
                layoutManager.scrollToPosition(scrollPosition)
            }
        }
    }

    override fun firstItemVisible(): Boolean {
        val manager = (layoutManager as LinearLayoutManager)
        val first = manager.findFirstVisibleItemPosition()
        val firstVisible = manager.findFirstCompletelyVisibleItemPosition()
        return first == firstVisible
    }
}