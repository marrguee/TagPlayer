package com.example.tagplayer.tag_settings.domain

import com.example.tagplayer.FakeHandleResponse
import com.example.tagplayer.tag_settings.presentation.TagSettingsResponse
import com.example.tagplayer.tag_settings.presentation.TagSettingsUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TagSettingsInteractorTest {
    private lateinit var interactor: TagSettingsInteractor

    private lateinit var repository: FakeRepository
    private lateinit var mapper: FakeMapper
    private lateinit var handleResponse: FakeHandleResponse.All<TagSettingsResponse>

    @Before
    fun setup() {
        repository = FakeRepository.Base()
        mapper = FakeMapper.Base()
        handleResponse = FakeHandleResponse.Empty(TagSettingsResponse.Empty)

        interactor = TagSettingsInteractor.Base(
            repository,
            mapper,
            handleResponse
        )
    }

    @Test
    fun tags() {
        val result: TagSettingsResponse = interactor.tags()
        handleResponse.checkHandleCalled(1)
        repository.checkTagsCalled(1)
    }

    @Test
    fun removeTag() = runBlocking {
        val tagId = 0L
        val result: TagSettingsResponse = interactor.remove(tagId)
        handleResponse.checkHandleAsyncEmptyCalled(1)
        repository.checkRemoveCalledWithId(tagId)
    }

    private interface FakeRepository : TagSettingsRepository<TagSettingsDomain> {
        fun checkTagsCalled(times: Int)
        fun checkRemoveCalledWithId(id: Long)

        class Base : FakeRepository {
            private var tagsCalled = 0
            private var tagId = 0L

            override fun checkTagsCalled(times: Int) = assertEquals(times, tagsCalled)

            override fun checkRemoveCalledWithId(id: Long) = assertEquals(id, tagId)

            override fun tags(): Flow<List<TagSettingsDomain>> {
                tagsCalled++
                return emptyFlow()
            }

            override suspend fun remove(id: Long) {
                tagId = id
            }

        }
    }

    private interface FakeMapper : TagSettingsDomain.Mapper<TagSettingsUi> {
        class Base : FakeMapper {
            override fun map(id: Long, title: String, color: String): TagSettingsUi =
                TagSettingsUi(id, title, color)
        }
    }
}