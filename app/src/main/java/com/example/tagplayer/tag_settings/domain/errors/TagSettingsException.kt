package com.example.tagplayer.tag_settings.domain.errors

abstract class TagSettingsException : Exception() {
    class Fetch : TagSettingsException()
    class Remove : TagSettingsException()
}