package com.example.tagplayer.filter

import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.domain.DispatcherList
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.presentation.HandleDeath
import com.example.tagplayer.core.presentation.viewmodel.RunAsync
import com.example.tagplayer.filter.data.FilterCacheDatasource
import com.example.tagplayer.filter.data.FilterRepositoryImpl
import com.example.tagplayer.filter.domain.FilterDomain
import com.example.tagplayer.filter.domain.FilterInteractor
import com.example.tagplayer.filter.domain.FilterResponse
import com.example.tagplayer.filter.domain.errors.FilterHandleDomainError
import com.example.tagplayer.filter.presentation.FilterObservable
import com.example.tagplayer.filter.presentation.FilterViewModel
import com.example.tagplayer.main.presentation.navigation.Navigation
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val filterModule = module {
    viewModel {
        val observable = FilterObservable()
        val repository = FilterRepositoryImpl(
            HandleTry.Base(FilterHandleDomainError.Base),
            FilterCacheDatasource.Base(get<TagsDao>()),
            SongTag.Mapper.ToFilterDomain
        )
        val interactor = FilterInteractor.Base(
            repository,
            FilterDomain.Mapper.Ui,
            HandleResponse.WithEmpty(
                FilterResponse.Empty,
                get<HandleError<DomainError, String>>()
            ) { error, mapper -> FilterResponse.Error(mapper.handle(error)) }
        )

        FilterViewModel(
            get<RunAsync>(),
            observable,
            interactor,
            FilterResponse.Mapper.Base(observable, DispatcherList.Base),
            get<Navigation.Navigate>(),
            get<HandleDeath>()
        )
    }
}