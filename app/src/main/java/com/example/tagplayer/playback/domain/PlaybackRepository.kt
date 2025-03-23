package com.example.tagplayer.playback.domain

interface PlaybackRepository<T, E> : HandleSongDetails.Mutable<T, E>