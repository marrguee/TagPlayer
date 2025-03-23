package com.example.tagplayer.playback.presentation

import android.widget.SeekBar

class SimpleSeekBarListener(private val seek: Seek) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) = Unit
    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        seekBar?.let { seek.seek((it.progress * 1000).toLong()) }
    }
}