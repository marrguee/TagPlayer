package com.example.tagplayer.core

interface UpdateUiObserver<T> {
    fun updateObserver(newObserver: ObserverUi<T>)
}

interface UpdateUi<T> {
    fun update(data: T)
}

interface Clear {
    fun clear()
}

interface Mutable<T> : UpdateUiObserver<T>, UpdateUi<T>, Clear

interface MutableGettingUpdates<T> {
    fun startGettingUpdates(observer: ObserverUi<T>)
    fun stopGettingUpdates()
}

interface ObservableUi<T> : Mutable<T> {

    abstract class ManualClear<T>(
        private val empty: T
    ) : ObservableUi<T> {
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
}