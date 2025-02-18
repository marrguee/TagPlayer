package com.example.tagplayer.tags_attach.presentation

import android.view.DragEvent
import android.view.View

class AttachDragListener(
    private val region: Int,
    private val block: (Long) -> Unit
) : View.OnDragListener {

    override fun onDrag(v: View?, event: DragEvent?): Boolean {
        if (v != null && event != null && event.action == DragEvent.ACTION_DROP && region == v.id) {
            val draggedItem = event.clipData.getItemAt(0)
            val id = draggedItem.text.toString().toLong()
            block.invoke(id)
        }
        return true
    }
}