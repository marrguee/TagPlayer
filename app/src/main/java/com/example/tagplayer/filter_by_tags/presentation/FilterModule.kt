package com.example.tagplayer.filter_by_tags.presentation

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.HandleDeath
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.SharedPrefs
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.filter_by_tags.domain.FilterTagsInteractor
import com.example.tagplayer.filter_by_tags.data.TagFilterCacheDatasource
import com.example.tagplayer.filter_by_tags.data.TagFilterRepositoryImpl
import com.example.tagplayer.filter_by_tags.domain.TagFilterDomain
import com.example.tagplayer.filter_by_tags.domain.TagFilterRepository
import com.example.tagplayer.home.presentation.TagFiltersState
import com.example.tagplayer.main.presentation.Navigation
import kotlinx.coroutines.flow.MutableStateFlow

class FilterModule(
    core: Core,
    sharedPrefs: SharedPrefs.Save<List<Long>>,
    private val tagFiltersObservable: MutableStateFlow<TagFiltersState>,
    private val clear: ClearViewModel
) : Module<FilterViewModel> {
    private val cacheDatasource: TagFilterCacheDatasource =
        TagFilterCacheDatasource.SharedPref(sharedPrefs, core.tagDao())
    private val repository: TagFilterRepository<TagFilterDomain> =
        TagFilterRepositoryImpl(cacheDatasource, SongTag.Mapper.ToFilterDomain)
    private val interactor: FilterTagsInteractor = FilterTagsInteractor.Base(repository)
    override fun create(): FilterViewModel {
        return FilterViewModel(
            clear,
            DispatcherList.Base,
            FilterObservable(),
            tagFiltersObservable,
            interactor,
            Navigation.Base,
            HandleDeath.Base(),
        )
    }
}