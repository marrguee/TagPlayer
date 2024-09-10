package com.example.tagplayer.search.presentation

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tagplayer.R
import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.core.domain.ProvideViewModel

class SearchFragment : Fragment(R.layout.search_fragment) {
    private val viewModel by lazy {
        (activity as ProvideViewModel).provide(SearchViewModel::class.java)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = listOf(
            SongUi(10000, "dsdad", "sadasda")
        )
        val adapter = ArrayAdapter<SearchUi>(requireContext(), R.layout.item_search)
        //adapter.setDropDownViewResource(R.layout.search_item)
//        val spinner =
//            view.findViewById<Spinner>(R.id.spinner)

        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val searchResultRecycler = view.findViewById<RecyclerView>(R.id.searchResultRecycler)
        val recyclerAdapter = SearchListenerAdapter {
            viewModel.playSongForeground(it)
        }

        searchResultRecycler.adapter = recyclerAdapter

        //spinner.adapter = adapter
        //autoCompleteTextView.threshold = 1
//        autoCompleteTextView.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_DONE ||
//                event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN
//            ) {
//                // Ваш код, который нужно выполнить при нажатии клавиши Enter
//                Toast.makeText(requireContext(), "Selected:", Toast.LENGTH_SHORT).show()
//                viewModel.updateHistory(autoCompleteTextView.text.trim().toString())
//                true // Возвращаем true, чтобы предотвратить добавление новой строки
//            } else {
//                Toast.makeText(requireContext(), "Not:", Toast.LENGTH_SHORT).show()
//                false
//            }
//        }

//        spinner.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                spinner.showDropDown()
//            }
//        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) return false
                viewModel.findSongs(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText.isNullOrEmpty()) {
//                    adapter.filter.filter("")
//                } else {
//                    adapter.filter.filter(newText)
//                }
                return true
            }
        })

        viewModel.observe(this) {
            it.dispatch(recyclerAdapter)
        }

        //viewModel.searchHistory()
    }
}
