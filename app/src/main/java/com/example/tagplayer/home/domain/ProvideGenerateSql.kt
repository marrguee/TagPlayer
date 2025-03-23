package com.example.tagplayer.home.domain

import com.example.tagplayer.home.data.GenerateSql

interface ProvideGenerateSql {
    fun generateSql() : GenerateSql
}