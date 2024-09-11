package com.example.tagplayer.filter_by_tags

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterTagsViewModel(
    private val communication: Communication<TagsFilterState>,
    private val selectedTagsLiveData: CustomObservable.Mutable<List<Long>>,
    private val interactor: FilterTagsInteractor,
    private val navigation: Navigation.Navigate,
) : ViewModel() {
    private var tagsIds: List<Long> = emptyList()
    private var allTags: MutableList<TagFilterUi> = mutableListOf()

    fun init() {
        selectedTagsLiveData.updateObserver(object : CustomObserver<List<Long>> {
            override fun update(data: List<Long>) {
                tagsIds = data
            }

        })
        viewModelScope.launch {
            allTags = interactor.tags() as MutableList
            if (tagsIds.isNotEmpty()) {
                allTags.forEach { tag ->
                    tagsIds.forEach { id ->
                        if (tag.compare(id)){
                            tag.changeSelected()
                        }
                    }
                }
            }
            withContext(Dispatchers.Main.immediate){
                communication.update(TagsFilterState.SelectedTagsChanged(allTags.toList()))
            }
        }
    }

    fun changeTagSelectedState(tagId: Long) {
        val tag = allTags.find { it.compare(tagId) }
        tag?.let {
            val newTag = it.copy().apply { changeSelected() }
            val index = allTags.indexOf(tag)
            allTags.removeAt(index)
            allTags.add(index, newTag)
            //it.changeSelected()
            communication.update(TagsFilterState.SelectedTagsChanged(allTags.toList()))
        }
    }

    fun applyFilter() {
        viewModelScope.launch {
            val selectedTagsIds = allTags.filter { it.selected() }.map { it.id() }
            interactor.applyFilter(selectedTagsIds)
            withContext(Dispatchers.Main.immediate) {
                selectedTagsLiveData.update(selectedTagsIds.toList())
                pop()
            }
        }
    }

    fun pop() {
        navigation.update(Screen.Pop)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in TagsFilterState>) {
        communication.observe(owner, observer)
    }
}