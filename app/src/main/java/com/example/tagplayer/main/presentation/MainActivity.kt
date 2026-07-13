package com.example.tagplayer.main.presentation

import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.tagplayer.R
import com.example.tagplayer.core.media_service.MediaObserver
import com.example.tagplayer.main.presentation.navigation.Screen
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModel()
    private val mediaObserver: MediaObserver by inject()

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : MainCallback {
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
}