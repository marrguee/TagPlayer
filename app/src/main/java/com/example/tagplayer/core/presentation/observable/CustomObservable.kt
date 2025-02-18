package com.example.tagplayer.core.presentation.observable

import androidx.annotation.MainThread
import com.example.tagplayer.core.presentation.HandleSaveRestoreState
import com.example.tagplayer.core.presentation.save_restore.SaveAndRestoreParcelable

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

    open class ManualClear<T>(
        protected val empty: T
    ) : All<T> {
        protected var observer: CustomObserver<T> = CustomObserver.Empty()
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

    open class ParcelableStateHandleManualClear<T>(
        empty: T
    ) : ManualClear<T>(empty), SaveAndRestoreParcelable<T> {
        override fun save(): T = cache

        override fun restore(data: T) {
            cache = data
        }

    }
}