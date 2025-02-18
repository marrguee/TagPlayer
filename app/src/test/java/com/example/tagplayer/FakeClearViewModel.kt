package com.example.tagplayer

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.domain.ClearViewModel
import org.junit.Assert

interface FakeClearViewModel: ClearViewModel {
    fun checkClearCalledWithClass(expected: Class<out ViewModel>)
    fun checkClearCalledTimes(expected: Int)

    class Base: FakeClearViewModel {
        private var clazz: Class<out ViewModel>? = null
        private var times: Int = 0

        override fun checkClearCalledWithClass(expected: Class<out ViewModel>) {
            Assert.assertEquals(expected, clazz)
        }

        override fun checkClearCalledTimes(expected: Int) {
            Assert.assertEquals(expected, times)
        }

        override fun clear(clazz: Class<out ViewModel>) {
            times++
            this.clazz = clazz
        }
    }
}