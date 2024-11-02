package com.example.tagplayer.main.presentation

import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.tagplayer.R
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.ProvideMediaObserver
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.home.presentation.HomeFragment

class MainActivity : AppCompatActivity(), ProvideViewModel {
    private val viewModel by lazy {
        (application as ProvideViewModel).provide(MainViewModel::class.java)
    }

    private val mediaObserver by lazy {
        (application as ProvideMediaObserver).mediaObserver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) { //todo change to state form
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, HomeFragment())
                .commit()
        }

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