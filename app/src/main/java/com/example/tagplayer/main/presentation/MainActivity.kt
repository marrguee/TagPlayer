package com.example.tagplayer.main.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.tagplayer.R
import com.example.tagplayer.home.presentation.HomeFragment
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.ProvideViewModel

class MainActivity : AppCompatActivity(), ProvideViewModel {
    private val viewModel by lazy {
        (application as ProvideViewModel).provide(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*        val viewPager = findViewById<ViewPager2>(R.id.pager)
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
                }*/

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, HomeFragment())
                .commit()
        }

        viewModel.startGettingUpdates(object : MainActivityCallback {
            override fun update(data: Screen) {
                data.dispatch(supportFragmentManager, R.id.mainContainer)
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

interface MainActivityCallback : CustomObserver<Screen> {
    object Empty : MainActivityCallback {
        override fun update(data: Screen) = Unit
    }
}