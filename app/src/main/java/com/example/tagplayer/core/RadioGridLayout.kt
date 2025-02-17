package com.example.tagplayer.core

import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid.PickerImageView
import com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid.ProvideColor
import com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid.RadioGridState
import com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid.RadioGridViewModel
import com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid.SelectColor

class RadioGridLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : GridLayout(context, attrs, defStyleAttr), View.OnClickListener, ProvideColor, SelectColor {

    private val viewModel: RadioGridViewModel by lazy {
        (this.context.applicationContext as ProvideViewModel)
            .provide(RadioGridViewModel::class.java)
    }

    fun init() {
        viewModel.init()
    }

    fun resume() {
        viewModel.startGettingUpdates(object : RadioGridObserver {
            override fun update(data: RadioGridState) {
                data.dispatch(children)
            }
        })
    }

    fun pause() {
        viewModel.stopGettingUpdates()
    }

    override fun color(): String =
        (children.filterIsInstance<PickerImageView>()
            .firstOrNull { it.selected() })
            ?.color() ?: String.format(
            "#%06X",
            (0xFFFFFF and ContextCompat.getColor(context, R.color.default_tag_color))
        )

    override fun onClick(view: View) {
        viewModel.selectAt(children.indexOf(view))
    }

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

    interface RadioGridObserver : CustomObserver<RadioGridState> {
        object Empty : RadioGridObserver {
            override fun update(data: RadioGridState) = Unit
        }
    }

    override fun select(color: String) = try {
        (children.first { (it as ProvideColor).color() == color } as SelectAndUnselect)
            .select()
    } catch (e: Exception) {
        (children.first() as SelectAndUnselect).select()
    }

}