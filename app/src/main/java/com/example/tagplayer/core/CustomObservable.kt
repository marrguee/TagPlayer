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

    interface HandleSaveAndRestoreState<T> {
        fun save(bundle: HandleSaveRestoreState.Save<T>)
        fun restore(bundle: HandleSaveRestoreState.Restore<T>)
    }

    interface Mutable<T> : UpdateUiObserver<T>, UpdateUi<T>

    interface All<T> : Mutable<T>, Clear

    interface AllHandleState<T> : All<T>, HandleSaveAndRestoreState<T>
    interface MutableHandleState<T> : Mutable<T>, HandleSaveAndRestoreState<T>

    open class ManualClear<T>(
        protected val empty: T
    ) : All<T> {
        private var observer: CustomObserver<T> = CustomObserver.Empty()
        protected var cache: T = empty

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
        private val empty: T,
        private val emptyObserver: CustomObserver<T>
    ) : Mutable<T> {
        private var observer: CustomObserver<T> = emptyObserver
        protected var cache: T = empty

        override fun updateObserver(newObserver: CustomObserver<T>) = synchronized(AutomaticClear::class) {
            observer = newObserver
            if (cache != empty && observer != emptyObserver) {
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

    open class StateHandleManualClear<T>(
        empty: T
    ) : ManualClear<T>(empty), AllHandleState<T> {
        override fun save(bundle: HandleSaveRestoreState.Save<T>) {
            bundle.save(cache)
        }
        override fun restore(bundle: HandleSaveRestoreState.Restore<T>) {
            cache = bundle.restore()
        }
    }

    open class StateHandleAutomaticClear<T>(
        empty: T,
        emptyObserver: CustomObserver<T>
    ) : AutomaticClear<T>(empty, emptyObserver), MutableHandleState<T> {
        override fun save(bundle: HandleSaveRestoreState.Save<T>) {
            bundle.save(cache)
        }
        override fun restore(bundle: HandleSaveRestoreState.Restore<T>) {
            cache = bundle.restore()
        }
    }

}