package com.example.tagplayer.main.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.OnFocusChangeListener
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.example.tagplayer.R
import com.example.tagplayer.core.ObservableUi
import com.example.tagplayer.core.ObserverUi
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.playback_control.presentation.PlaybackControlFragment
import com.example.tagplayer.search.presentation.SearchFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), ProvideViewModel {
    private val viewModel by lazy {
        (application as ProvideViewModel).provide(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager2>(R.id.pager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, ManageTab.Base)
        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            tab.text = ManageTabText(this).invoke(pos)
        }.attach()

        val searchField = findViewById<SearchView>(R.id.searchEdit)
        searchField.onFocusChangeListener = OnFocusChangeListener {
                _, hasFocus -> if (hasFocus) supportFragmentManager.beginTransaction()
            .add(R.id.searchContainer, SearchFragment())
            .addToBackStack("SearchFragment")
            .commit()
        }

        viewModel.startGettingUpdates(object : MainActivityCallback {
            override fun update(data: Screen) {
                data.dispatch(supportFragmentManager, R.id.searchContainer)
                data.consumed(viewModel)
            }
        })

    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun <T : ViewModel> provide(clazz: Class<out T>) =
        (application as ProvideViewModel).provide(clazz)
}

interface MainActivityCallback : ObserverUi<Screen> {
    object Empty : MainActivityCallback {
        override fun update(data: Screen) = Unit
    }
}