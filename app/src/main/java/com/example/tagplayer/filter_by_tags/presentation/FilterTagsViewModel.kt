package com.example.tagplayer.filter_by_tags.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.filter_by_tags.domain.FilterTagsInteractor
import com.example.tagplayer.home.presentation.TagFiltersResponse
import com.example.tagplayer.main.presentation.ComebackViewModel
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterTagsViewModel(
    private val observable: CustomObservable.All<TagsFilterState>,
    private val tagFiltersObservable: CustomObservable.Mutable<TagFiltersResponse>,
    private val interactor: FilterTagsInteractor,
    private val navigation: Navigation.Navigate,
    clear: ClearViewModel
) : ComebackViewModel(clear), HandleUiStateUpdates.All<TagsFilterState> {
    private var allTags: MutableList<TagFilterUi> = mutableListOf()

    fun init() {
        viewModelScope.launch {
            allTags = interactor.tags() as MutableList
            tagFiltersObservable.updateObserver(object : CustomObserver<TagFiltersResponse> {
                override fun update(data: TagFiltersResponse) {
                    data.mapIntoAllList(allTags)
                }
            })
            withContext(Dispatchers.Main.immediate){
                observable.update(TagsFilterState.SelectedTagsChanged(allTags.toList()))
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
            observable.update(TagsFilterState.SelectedTagsChanged(allTags.toList()))
        }
    }

    fun applyFilter() {
        viewModelScope.launch {
            val selectedTagsIds = allTags.filter { it.selected() }.map { it.id() }
            interactor.applyFilter(selectedTagsIds)
            withContext(Dispatchers.Main.immediate) {
                tagFiltersObservable.update(
                    if(selectedTagsIds.isEmpty()) TagFiltersResponse.EmptyList
                    else TagFiltersResponse.FilledList(selectedTagsIds.toList())
                )
                comeback()
            }
        }
    }

    override fun startGettingUpdates(observer: CustomObserver<TagsFilterState>) {
        observable.updateObserver(observer)
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(FilterTagObserver.Empty)
    }

    override fun clear() {
        observable.clear()
    }

    override fun comeback() {
        super.comeback()
        navigation.update(Screen.Pop)
    }
}