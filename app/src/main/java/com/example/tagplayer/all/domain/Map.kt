package com.example.tagplayer.all.domain

interface Map {
    fun <T> map(modelMapper: ModelMapper<T>) : T
}