package com.example.tagplayer.core.data.database

import androidx.room.DeleteColumn
import androidx.room.migration.AutoMigrationSpec

@DeleteColumn(tableName = "last_played", columnName = "time")
class TwoToThreeMigration : AutoMigrationSpec

