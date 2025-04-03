package com.example.tagplayer.home.presentation

import android.Manifest
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.ManageResources

interface HandleDeclineText {
    fun getDescription(): String

    interface ProvideHandleDeclineText {
        fun permissionTextProvider(permission: String) : HandleDeclineText
    }

    abstract class Base(
        private val id: Int,
        private val manageStrings: ManageResources.Strings
    ) : HandleDeclineText {
        override fun getDescription(): String = manageStrings.string(id)
    }

    data class Storage(
        private val manageStrings: ManageResources.Strings
    ) : Base(R.string.declined_storage, manageStrings)

    data class Notifications(
        private val manageStrings: ManageResources.Strings
    ) : Base(R.string.declined_notification, manageStrings)

    class Factory(
        private val manageStrings: ManageResources.Strings
    ) : ProvideHandleDeclineText {
        override fun permissionTextProvider(permission: String) = when(permission) {
            Manifest.permission.POST_NOTIFICATIONS -> Notifications(manageStrings)
            else -> Storage(manageStrings)
        }
    }
}