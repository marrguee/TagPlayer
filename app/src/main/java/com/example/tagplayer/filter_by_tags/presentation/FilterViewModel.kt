package com.example.tagplayer.filter_by_tags.presentation

import androidx.lifecycle.viewModelScope
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.CustomObserver
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.core.HandleSaveRestoreState
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.filter_by_tags.domain.FilterTagsInteractor
import com.example.tagplayer.home.presentation.TagFiltersState
import com.example.tagplayer.main.presentation.ComebackViewModel
import com.example.tagplayer.main.presentation.HandleSaveAndRestoreState
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.main.presentation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FilterViewModel(
    clear: ClearViewModel,
    private val dispatcherList: DispatcherList,
    private val observable: CustomObservable.AllHandleState<FilterScreenState>,
    private val tagFiltersObservable: MutableStateFlow<TagFiltersState>,
    private val interactor: FilterTagsInteractor,
    private val navigation: Navigation.Navigate,
    private val handleDeath: HandleDeath,
    private var allTags: MutableList<FilterUi> = mutableListOf()
) : ComebackViewModel(clear), HandleUiStateUpdates.All<FilterScreenState>,
    HandleSaveAndRestoreState<FilterScreenState> {

    override fun init(bundle: HandleSaveRestoreState.Restore<FilterScreenState>) {
        if (bundle.empty()) {
            viewModelScope.launch(dispatcherList.io()) {
                allTags = interactor.tags() as MutableList
                tagFiltersObservable.value.mapIntoAllList(allTags)
                withContext(dispatcherList.ui()) {
                    observable.update(FilterScreenState.SelectedChangedScreen(allTags.toList()))
                }
            }
            handleDeath.handleFirstStart()
        } else if (handleDeath.deathHappened()) {
            observable.restore(bundle)
            handleDeath.handleDeath()
        }
    }

    fun changeTagSelectedState(tagId: Long) {
        val tag = allTags.find { it.compare(tagId) }
        tag?.let {
            val newTag = it.copy().apply { changeSelected() }
            val index = allTags.indexOf(tag)
            allTags.removeAt(index)
            allTags.add(index, newTag)
            observable.update(FilterScreenState.SelectedChangedScreen(allTags.toList()))
        }
    }

    fun applyFilter() {
        viewModelScope.launch {
            val selectedTagsIds = allTags.filter { it.selected() }.map { it.id() }
            interactor.applyFilter(selectedTagsIds)
            withContext(dispatcherList.ui()) {
                tagFiltersObservable.emit(
                    if (selectedTagsIds.isEmpty()) TagFiltersState.EmptyList
                    else TagFiltersState.FilledList(selectedTagsIds.toList())
                )
                comeback()
            }
        }
    }

    fun clearFilter() {
        val newList = allTags.map {
            if (it.selected()) it.copy().apply { changeSelected() } else it
        }
        allTags = newList.toMutableList()
        observable.update(FilterScreenState.SelectedChangedScreen(newList.toList()))
    }

    override fun startGettingUpdates(observer: CustomObserver<FilterScreenState>) {
        observable.updateObserver(observer)
    }

    override fun stopGettingUpdates() {
        observable.updateObserver(FilterObserver.Empty)
    }

    override fun clear() {
        observable.clear()
    }

    override fun comeback() {
        super.comeback()
        navigation.update(Screen.Pop)
    }

    override fun save(bundle: HandleSaveRestoreState.Save<FilterScreenState>) {
        observable.save(bundle)
    }
}