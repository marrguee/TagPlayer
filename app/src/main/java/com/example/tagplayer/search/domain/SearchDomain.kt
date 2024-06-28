package com.example.tagplayer.search.domain

import com.example.tagplayer.search.presentation.SearchUi

data class SearchDomain(
    private val searchRequest: String
) {
    interface Mapper<T> {
        fun map(searchRequest: String) : T

        object ToPresentation : Mapper<SearchUi> {
            override fun map(searchRequest: String) = SearchUi(searchRequest)
        }
    }

    fun <T> map(mapper: Mapper<T>) : T = mapper.map(searchRequest)
}