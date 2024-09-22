package com.example.tagplayer.filter_by_tags

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.Communication
import com.example.tagplayer.main.presentation.Navigation

class TagsFilterModule(
    private val core: Core,
    private val sharedPrefs: SharedPrefs.Save<List<Long>>,
    private val selectedTagsObservable: CustomObservable.Mutable<List<Long>>,
    private val clear: ClearViewModel
) : Module<FilterTagsViewModel> {
    private val cacheDatasource: TagFilterCacheDatasource =
        TagFilterCacheDatasource.SharedPref(sharedPrefs, core.tagDao())
    private val repository: TagFilterRepository<TagFilterDomain> =
        TagFilterRepositoryImpl(cacheDatasource, SongTag.Mapper.ToFilterDomain)
    private val interactor: FilterTagsInteractor = FilterTagsInteractor.Base(repository)
    override fun create(): FilterTagsViewModel {
        return FilterTagsViewModel(
            FilterTagObservable(),
            selectedTagsObservable,
            interactor,
            Navigation.Base,
            clear
        )
    }
}