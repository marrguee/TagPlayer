package com.example.tagplayer.home.data

import androidx.sqlite.db.SimpleSQLiteQuery

interface GenerateSql {
    fun orderBy(field: ObtainFieldName): SimpleSQLiteQuery

    abstract class Base(private val type: String) : GenerateSql {
        override fun orderBy(field: ObtainFieldName): SimpleSQLiteQuery = SimpleSQLiteQuery(
            "SELECT *, data_modified, title FROM songs " +
                "WHERE EXISTS (SELECT 1 FROM tags WHERE selected = 1) " +
                "AND songs.id IN (SELECT sat.track_id FROM songs_and_tags sat " +
                "WHERE sat.tag_id IN (SELECT id FROM tags WHERE selected = 1) " +
                "GROUP BY sat.track_id HAVING " +
                "COUNT(DISTINCT sat.tag_id) = (SELECT COUNT(*) FROM tags WHERE selected = 1)) " +
                "UNION ALL SELECT *, data_modified, title FROM songs " +
                "WHERE NOT EXISTS (SELECT 1 FROM tags WHERE selected = 1) " +
                "ORDER BY ${field.name()} $type"
        )
    }

    object Asc : Base("ASC")

    object Desc : Base("DESC")
}