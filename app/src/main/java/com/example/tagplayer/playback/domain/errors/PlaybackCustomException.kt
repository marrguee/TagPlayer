package com.example.tagplayer.playback.domain.errors

abstract class PlaybackCustomException : Exception() {
    class FetchTagsException : PlaybackCustomException()
    class ShareException : PlaybackCustomException()
    class DeleteException : PlaybackCustomException()
}