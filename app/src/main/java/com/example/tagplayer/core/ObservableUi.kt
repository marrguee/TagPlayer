package com.example.tagplayer.core


interface MutableGettingUpdates<T> {
    fun startGettingUpdates(observer: ObserverUi<T>)
    fun stopGettingUpdates()
}

interface ObservableUi<T> {

    interface UpdateUiObserver<T> {
        fun updateObserver(newObserver: ObserverUi<T>)
    }

    interface UpdateUi<T> {
        fun update(data: T)
    }

    interface Clear {
        fun clear()
    }

    interface Mutable<T> : UpdateUiObserver<T>, UpdateUi<T>

    interface All<T> : Mutable<T>, Clear

    abstract class ManualClear<T>(
        private val empty: T
    ) : All<T> {
        private var observer: ObserverUi<T> = ObserverUi.Empty()
        private var cache: T = empty

        override fun updateObserver(newObserver: ObserverUi<T>) = synchronized(ManualClear::class) {
            observer = newObserver
            observer.update(cache)
        }

        override fun update(data: T) = synchronized(ManualClear::class) {
            cache = data
            observer.update(cache)
        }

        override fun clear() {
            cache = empty
        }
    }

    abstract class AutomaticClear<T>(
        private val empty: T
    ) : Mutable<T> {
        private var observer: ObserverUi<T> = ObserverUi.Empty()
        private var cache: T = empty

        override fun updateObserver(newObserver: ObserverUi<T>) = synchronized(AutomaticClear::class) {
            observer = newObserver
            if (cache != empty) {
                observer.update(cache)
                cache = empty
            }
        }

        override fun update(data: T) = synchronized(AutomaticClear::class) {
            cache = data
            observer.update(cache)
        }
    }
}