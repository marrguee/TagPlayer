package com.example.tagplayer.core.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface RunAsync {
    fun <T> run(
        coroutineScope: CoroutineScope,
        block: suspend () -> T,
        uiBlock: (data: T) -> Unit
    )

    class Base(
        private val dispatcherList: DispatcherList
    ) : RunAsync {
        override fun <T> run(
            coroutineScope: CoroutineScope,
            block: suspend () -> T,
            uiBlock: (data: T) -> Unit
        ) {
            coroutineScope.launch(dispatcherList.io()) {
                val result = block.invoke()
                withContext(dispatcherList.ui()) {
                    uiBlock.invoke(result)
                }
            }
        }
    }
}