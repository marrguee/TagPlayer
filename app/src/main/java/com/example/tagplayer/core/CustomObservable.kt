package com.example.tagplayer.core

import androidx.annotation.MainThread

interface CustomObservable {

    interface UpdateUiObserver<T> {
        @MainThread
        fun updateObserver(newObserver: CustomObserver<T>)
    }

    interface UpdateUi<T> {
        @MainThread
        fun update(data: T)
    }

    interface Clear {
        fun clear()
    }

    interface Mutable<T> : UpdateUiObserver<T>, UpdateUi<T>

    interface All<T> : Mutable<T>, Clear

    open class ManualClear<T>(
        private val empty: T
    ) : All<T> {
        private var observer: CustomObserver<T> = CustomObserver.Empty()
        private var cache: T = empty

        override fun updateObserver(newObserver: CustomObserver<T>) = synchronized(ManualClear::class) {
            observer = newObserver
            observer.update(cache)
        }

        override fun update(data: T) = synchronized(ManualClear::class) {
            if (cache != data) {
                cache = data
                observer.update(cache)
            }
        }

        override fun clear() {
            cache = empty
        }
    }

    open class AutomaticClear<T>(
        private val empty: T
    ) : Mutable<T> {
        private var observer: CustomObserver<T> = CustomObserver.Empty()
        private var cache: T = empty

        override fun updateObserver(newObserver: CustomObserver<T>) = synchronized(AutomaticClear::class) {
            observer = newObserver
            if (cache != empty) {
                observer.update(cache)
                cache = empty
            }
        }

        override fun update(data: T) = synchronized(AutomaticClear::class) {
            if (cache != data) {
                cache = data
                observer.update(cache)
            }
        }
    }
}