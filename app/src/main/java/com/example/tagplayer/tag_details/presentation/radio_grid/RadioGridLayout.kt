package com.example.tagplayer.tag_details.presentation.radio_grid

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.core.view.children
import com.example.tagplayer.core.presentation.custom_views.interfaces.SelectAndUnselect
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.core.presentation.save_restore.SelectState
import com.example.tagplayer.core.presentation.viewmodel.HandleComeback

class RadioGridLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : GridLayout(context, attrs, defStyleAttr), View.OnClickListener, ProvideColor,
    SelectColor, HandleComeback {

    private val viewModel: RadioGridViewModel by lazy {
        (this.context.applicationContext as ProvideViewModel)
            .provide(RadioGridViewModel::class.java)
    }

    fun init() = viewModel.init()

    fun resume() = viewModel.startGettingUpdates(object : RadioGridObserver {
        override fun update(data: RadioGridState) {
            data.dispatch(children)
        }
    })

    fun pause() = viewModel.stopGettingUpdates()

    override fun comeback() = viewModel.comeback()

    override fun color(): String =
        (children.firstOrNull { (it as SelectAndUnselect).selected() } as ProvideColor).color()

    override fun onClick(view: View) = viewModel.selectAt(children.indexOf(view))

    override fun select(color: String) = viewModel.selectByColor(color)

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        child?.setOnClickListener(this)
    }

    override fun onSaveInstanceState(): Parcelable? = super.onSaveInstanceState()?.let {
        val selectedState = SelectState(it)
        selectedState.save(viewModel.save())
        selectedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val selectedState = state as SelectState
        super.onRestoreInstanceState(state)
        selectedState.restore(viewModel)
    }
}