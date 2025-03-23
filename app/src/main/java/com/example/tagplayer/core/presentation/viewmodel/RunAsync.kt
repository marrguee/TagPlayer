package com.example.tagplayer.core.presentation.viewmodel

import com.example.tagplayer.core.domain.DispatcherList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface RunAsync {
    fun <T : Any> handle(scope: CoroutineScope, uiBlock: (T) -> Unit, block: suspend () -> T)
    fun <T : Any> handleMultiply(
        scope: CoroutineScope,
        flag: Boolean,
        uiBlock: (T) -> Unit,
        block: suspend () -> T
    )

    class Base(private val dispatcherList: DispatcherList) : RunAsync {
        override fun <T : Any> handle(
            scope: CoroutineScope,
            uiBlock: (T) -> Unit,
            block: suspend () -> T
        ) = handleMultiply(scope, false, uiBlock, block)

        override fun <T : Any> handleMultiply(
            scope: CoroutineScope,
            flag: Boolean,
            uiBlock: (T) -> Unit,
            block: suspend () -> T
        ) {
            scope.launch(dispatcherList.io()) {
                do {
                    val response = block.invoke()
                    withContext(dispatcherList.ui()) {
                        uiBlock.invoke(response)
                    }
                } while (flag)
            }
        }
    }
}