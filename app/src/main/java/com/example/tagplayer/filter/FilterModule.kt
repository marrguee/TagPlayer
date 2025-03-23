package com.example.tagplayer.filter

import com.example.tagplayer.core.Core
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.Module
import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.observable.CustomObservable
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.filter.domain.FilterInteractor
import com.example.tagplayer.filter.data.FilterCacheDatasource
import com.example.tagplayer.filter.data.FilterRepositoryImpl
import com.example.tagplayer.filter.domain.FilterDomain
import com.example.tagplayer.filter.domain.errors.FilterHandleDomainError
import com.example.tagplayer.filter.domain.FilterRepository
import com.example.tagplayer.filter.domain.FilterResponse
import com.example.tagplayer.filter.presentation.FilterObservable
import com.example.tagplayer.filter.presentation.FilterState
import com.example.tagplayer.filter.presentation.FilterViewModel
import com.example.tagplayer.main.presentation.navigation.Navigation

class FilterModule(
    core: Core,
    private val clear: ClearViewModel,
) : Module<FilterViewModel> {
    private val observable: CustomObservable.AllHandleState<FilterState> = FilterObservable()
    private val cacheDatasource: FilterCacheDatasource = FilterCacheDatasource.Base(core.tagDao())
    private val repository: FilterRepository<FilterDomain> = FilterRepositoryImpl(
        HandleTry.Base(FilterHandleDomainError.Base),
        cacheDatasource,
        SongTag.Mapper.ToFilterDomain,
    )
    private val interactor: FilterInteractor = FilterInteractor.Base(
        repository,
        FilterDomain.Mapper.Ui,
        HandleResponse.WithEmpty(
            FilterResponse.Empty,
            core.handlePresentationError()
        ) { e, handleError ->
            FilterResponse.Error(handleError.handle(e))
        }
    )

    override fun create(): FilterViewModel {
        return FilterViewModel(
            clear,
            RunAsync.Base(DispatcherList.Base),
            observable,
            interactor,
            FilterResponse.Mapper.Base(observable, DispatcherList.Base),
            Navigation.Base,
            HandleDeath.Base(),
        )
    }
}