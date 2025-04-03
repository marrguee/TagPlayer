package com.example.tagplayer.home.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.core.presentation.fragments.BindingFragment
import com.example.tagplayer.core.presentation.generic_adapter.item_interfaces.MenuAction
import com.example.tagplayer.databinding.FragmentHomeBinding

class HomeFragment : BindingFragment<FragmentHomeBinding>() {
    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(HomeViewModel::class.java)
    }
    private lateinit var recentlyAdapter: HomeAdapter

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            viewModel.scan()
            return@registerForActivityResult
        }
        permissions.forEach {
            if (it.value) return@forEach
            viewModel.handlePermission(it.key)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuOptions = listOf(
            Pair(
                R.id.editSongTagsMenu,
                object : MenuAction {
                    override fun action(vararg args: Any) {
                        viewModel.attachTagsScreen(args[0] as Long)
                    }
                }
            )
        )
        recentlyAdapter = HomeAdapter(menuOptions) { viewModel.play(it) }

        with(binding) {
            recentlyRecycler.adapter = recentlyAdapter
            PagerSnapHelper().attachToRecyclerView(recentlyRecycler)
            libraryRecycler.adapter = HomeAdapter(menuOptions) { id ->
                viewModel.play(id)
            }

            tagFilterButton.apply {
                setOnLongClickListener {
                    viewModel.tagSettingsScreen()
                    true
                }
                setOnClickListener {
                    viewModel.filterTagsScreen()
                }
            }

            recentlyTextView.setOnClickListener { viewModel.recentlyPlayedScreen() }

            searchEditText.setOnFocusChangeListener { _, hasFocus ->
                if(hasFocus) viewModel.searchScreen()
            }

            sortSpinner.adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_options,
                R.layout.item_spinner_sort
            ).apply {
                setDropDownViewResource(R.layout.item_spinner_sort_drop_down)
            }

            sortSpinner.onItemSelectedListener = SortingTypeListener.Base(viewModel)
        }
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) binding.motionLayout.transitionState = savedInstanceState
    }

    override fun onStart() {
        super.onStart()
        requestPermissions()
        viewModel.loadRecently()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates(object : HomeObserver {
            override fun update(data: HomeState) {
                data.dispatch(
                    binding.libraryPlaceholder,
                    recentlyAdapter,
                    binding.libraryRecycler,
                    binding.motionLayout
                )
                data.consumed(viewModel)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putAll(binding.motionLayout.transitionState)
    }

    private fun requestPermissions() {
        val permissionsToRequest = permissions()
        requestPermissionsLauncher.launch(permissionsToRequest)
    }

    private fun permissions(): Array<String> =
        mutableListOf<String>().also {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                it.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
                    it.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                it.add(Manifest.permission.READ_MEDIA_AUDIO)
                it.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toTypedArray()
}