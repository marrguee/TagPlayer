package com.example.tagplayer.main.presentation

import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.tagplayer.R
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.ProvideMediaObserver
import com.example.tagplayer.core.domain.ProvideViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main), ProvideViewModel {
    private val viewModel by lazy {
        (application as ProvideViewModel).provide(MainViewModel::class.java)
    }

    private val mediaObserver by lazy {
        (application as ProvideMediaObserver).mediaObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : MainActivityCallback {
            override fun update(data: Screen) {
                data.dispatch(supportFragmentManager, R.id.mainContainer)
                data.consumed(viewModel)
            }
        })
        contentResolver.registerContentObserver(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            true,
            mediaObserver
        )
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
        contentResolver.unregisterContentObserver(mediaObserver)
    }

    override fun <T : ViewModel> provide(clazz: Class<out T>) =
        (application as ProvideViewModel).provide(clazz)
}

interface MainActivityCallback : CustomObserver<Screen> {
    object Empty : MainActivityCallback {
        override fun update(data: Screen) = Unit
    }
}