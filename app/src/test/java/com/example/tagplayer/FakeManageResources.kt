package com.example.tagplayer

import com.example.tagplayer.core.domain.ManageResources

interface FakeManageResources : ManageResources.All {
    class Base : FakeManageResources {
        override fun string(id: Int): String = "string"

        override fun notificationChannelName(): String = "notificationChannelName"

        override fun notificationChannelId(): String = "notificationChannelId"

        override fun songIdError(): String = "songIdError"

        override fun retrieveIdError(): String = "retrieveIdError"
    }
}