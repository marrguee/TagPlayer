package com.example.tagplayer.main.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.tagplayer.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager2>(R.id.pager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, ManageTab.Base)
        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            tab.text = ManageTabText(this).invoke(pos)
        }.attach()
    }
}