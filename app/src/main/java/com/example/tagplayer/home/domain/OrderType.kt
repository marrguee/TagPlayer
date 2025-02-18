package com.example.tagplayer.home.domain

import com.example.tagplayer.home.data.GenerateSql

interface OrderType : ProvideGenerateSql {

    object Asc : OrderType {
        override fun generateSql(): GenerateSql = GenerateSql.Asc
    }

    object Desc : OrderType {
        override fun generateSql(): GenerateSql = GenerateSql.Desc
    }
}