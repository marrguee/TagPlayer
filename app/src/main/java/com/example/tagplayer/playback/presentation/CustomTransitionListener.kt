package com.example.tagplayer.playback.presentation

import androidx.constraintlayout.motion.widget.MotionLayout

interface CustomTransitionListener : MotionLayout.TransitionListener {

    abstract class Motion : CustomTransitionListener {
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) =
            Unit

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) = Unit

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) = Unit

        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) = Unit
    }

    object Disable : Motion() {
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
            motionLayout?.progress = 0f
        }
    }

    object Enable : Motion()
}