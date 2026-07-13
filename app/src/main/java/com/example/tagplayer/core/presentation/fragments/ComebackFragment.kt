package com.example.tagplayer.core.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.viewbinding.ViewBinding
import com.example.tagplayer.core.presentation.viewmodel.ComebackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModelForClass
import java.lang.reflect.ParameterizedType

abstract class ComebackFragment<B : ViewBinding, V : ComebackViewModel> : BindingFragment<B>() {

    @Suppress("UNCHECKED_CAST")
    protected val viewModel: V by viewModelForClass(
        ((javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[VIEWMODEL_CLASS_INDEX] as Class<V>)
            .kotlin
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() = viewModel.comeback()
            }
        )
    }

    companion object {
        private const val VIEWMODEL_CLASS_INDEX = 1
    }
}