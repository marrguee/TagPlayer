package com.example.tagplayer.all.presentation

interface AllState {

    fun dispatch(recyclerAdapter: AllRecyclerAdapter)

    class TracksUpdated(
        private val list: List<TrackUi>
    ) : AllState {
        override fun dispatch(recyclerAdapter: AllRecyclerAdapter) {
            recyclerAdapter.update(list)
        }
    }

    class Error(private val msg: String) : AllState {
        override fun dispatch(recyclerAdapter: AllRecyclerAdapter) = Unit
    }
}