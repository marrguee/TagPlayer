package com.example.tagplayer.core.data.database

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

@DeleteTable(tableName = "search_history")
class FifeToSixMigration : AutoMigrationSpec