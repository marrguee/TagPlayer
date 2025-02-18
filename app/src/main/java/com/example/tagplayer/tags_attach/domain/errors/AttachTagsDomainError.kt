package com.example.tagplayer.tags_attach.domain.errors

import com.example.tagplayer.R
import com.example.tagplayer.core.domain.DomainError

interface AttachTagsDomainError {
    class All : AttachTagsDomainError, DomainError(R.string.failed_fetch_all_tags)
    class Owned : AttachTagsDomainError, DomainError(R.string.failed_fetch_owned_tags)
    class Add : AttachTagsDomainError, DomainError(R.string.failed_add_tag_to_song)
    class Remove : AttachTagsDomainError, DomainError(R.string.failed_remove_tag_to_song)
}