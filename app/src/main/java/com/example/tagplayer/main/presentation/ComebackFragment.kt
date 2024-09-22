package com.example.tagplayer.main.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import com.example.tagplayer.core.domain.ProvideViewModel
import java.lang.reflect.ParameterizedType

abstract class ComebackFragment<B : ViewBinding, V : ComebackViewModel> : BindingFragment<B>() {
    protected lateinit var viewModel: V

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val vmClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<V>
        viewModel = (activity as ProvideViewModel).provide(vmClass)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.comeback()
            }
        })
    }
}