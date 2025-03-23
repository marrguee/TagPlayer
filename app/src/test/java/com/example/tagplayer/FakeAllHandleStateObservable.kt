package com.example.tagplayer

import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import com.example.tagplayer.core.presentation.HandleSaveRestoreState
import org.junit.Assert.assertEquals

interface FakeAllHandleStateObservable<T> : CustomObservable.AllHandleState<T>,
    FakeAllObservable<T> {
    fun checkSaveInstanceCalledTimes(expected: Int)
    fun checkRestoreInstanceCalledTimes(expected: Int)

    abstract class Base<T>(
        empty: T,
        observer: CustomObserver<T>
    ) : FakeAllHandleStateObservable<T>, FakeAllObservable.Base<T>(empty, observer) {
        private var save: Int = 0
        private var restore: Int = 0

        override fun checkSaveInstanceCalledTimes(expected: Int) {
            assertEquals(expected, save)
        }

        override fun checkRestoreInstanceCalledTimes(expected: Int) {
            assertEquals(expected, restore)
        }

        override fun save(bundle: HandleSaveRestoreState.Save<T>) {
            save++
            bundle.save(state)
        }

        override fun restore(bundle: HandleSaveRestoreState.Restore<T>) {
            restore++
            state = bundle.restore()
        }
    }
}