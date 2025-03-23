package com.example.tagplayer.tag_settings.data

import com.example.tagplayer.FakeForegroundWrapper
import com.example.tagplayer.FakeHandleTry
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tag_settings.domain.TagSettingsDomain
import com.example.tagplayer.tag_settings.domain.TagSettingsRepository
import com.example.tagplayer.tag_settings.domain.errors.TagSettingsException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TagSettingsRepositoryTest {
    private lateinit var repository: TagSettingsRepository<TagSettingsDomain>

    private lateinit var foregroundWrapper: FakeForegroundWrapper
    private lateinit var handleTry: FakeHandleTry<TagSettingsException>
    private lateinit var cacheDatasource: FakeCacheDatasource
    private lateinit var mapper: FakeMapper

    @Before
    fun setup() {
        foregroundWrapper = FakeForegroundWrapper.Base()
        handleTry = FakeHandleTry.Base()
        cacheDatasource = FakeCacheDatasource.Base()
        mapper = FakeMapper.Base()

        repository = TagSettingsRepositoryImpl(
            foregroundWrapper,
            handleTry,
            cacheDatasource,
            mapper
        )
    }

    @Test
    fun tags() {
        val result: Flow<List<TagSettingsDomain>> = repository.tags()
        handleTry.checkHandleCalled(1)
        cacheDatasource.checkTagsCalled(1)
    }

    @Test
    fun removeTag() = runBlocking {
        val tagId = 0L
        repository.remove(tagId)
        handleTry.checkHandleAsyncCalled(1)
        cacheDatasource.checkRemoveCalledWithId(tagId)
    }

    private interface FakeCacheDatasource : TagSettingsCacheDatasource {
        fun checkTagsCalled(times: Int)
        fun checkRemoveCalledWithId(id: Long)

        class Base : FakeCacheDatasource {
            private var tagsCalled = 0
            private var tagId = 0L

            override fun checkTagsCalled(times: Int) = assertEquals(times, tagsCalled)

            override fun checkRemoveCalledWithId(id: Long) = assertEquals(id, tagId)

            override fun tags(): Flow<List<SongTag>> {
                tagsCalled++
                return emptyFlow()
            }

            override suspend fun remove(id: Long) {
                tagId = id
            }
        }
    }

    private interface FakeMapper : SongTag.Mapper<TagSettingsDomain> {
        class Base : FakeMapper {
            override fun map(
                id: Long,
                title: String,
                color: String,
                selected: Boolean
            ): TagSettingsDomain = TagSettingsDomain(id, title, color)
        }
    }
}