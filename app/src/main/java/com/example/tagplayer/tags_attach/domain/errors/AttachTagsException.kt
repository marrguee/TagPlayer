package com.example.tagplayer.tags_attach.domain.errors

abstract class AttachTagsException : Exception() {
    class All : AttachTagsException()

    class Owned : AttachTagsException()

    class Add : AttachTagsException()

    class Remove : AttachTagsException()
}