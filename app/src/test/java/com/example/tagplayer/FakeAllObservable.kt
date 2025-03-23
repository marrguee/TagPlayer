package com.example.tagplayer

import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.observable.CustomObserver
import org.junit.Assert.assertEquals

interface FakeAllObservable<T> : CustomObservable.All<T> {
    fun checkObserver(expected: CustomObserver<T>)
    fun checkState(expected: T)
    fun checkClearTimes(expected: Int)
    fun runClear()

    abstract class Base<T>(
        private val empty: T,
        private var observer: CustomObserver<T>
    ) : FakeAllObservable<T> {
        protected var state: T = empty
        private var clearTimes: Int = 0

        override fun checkObserver(expected: CustomObserver<T>) = assertEquals(expected, observer)

        override fun checkState(expected: T) = assertEquals(expected, state)

        override fun checkClearTimes(expected: Int) = assertEquals(expected, clearTimes)

        override fun updateObserver(newObserver: CustomObserver<T>) {
            observer = newObserver
            observer.update(state)
        }

        override fun update(data: T) {
            if (state != data) {
                state = data
                observer.update(state)
            }
        }

        override fun clear() {
            clearTimes++
        }

        override fun runClear() {
            state = empty
        }
    }
}