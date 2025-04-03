package com.example.tagplayer.playback.data

import com.example.tagplayer.FakeHandleTry
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.playback.domain.PlaybackRepository
import com.example.tagplayer.playback.domain.TagPlaybackDomain
import com.example.tagplayer.playback.domain.errors.PlaybackCustomException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PlaybackRepositoryTest {
    private lateinit var repository: PlaybackRepository<TagPlaybackDomain, HandleMediaResult>

    private lateinit var handleTry: FakeHandleTry<PlaybackCustomException>
    private lateinit var cacheDatasource: FakeCacheDatasource
    private lateinit var mapper: FakeMapper

    @Before
    fun setUp() {
        handleTry = FakeHandleTry.Base()
        cacheDatasource = FakeCacheDatasource.Base()
        mapper = FakeMapper.Base()
        repository = PlaybackRepositoryImpl(
            handleTry,
            cacheDatasource,
            mapper
        )
    }

    @Test
    fun `test tags`() {
        val songId = 123L
        val result: Flow<List<TagPlaybackDomain>> = repository.tags(songId)
        cacheDatasource.checkTagsCalled(1)
    }

    @Test
    fun `test uri return`() = runBlocking {
        val songId = 123L
        val result: String = repository.uri(songId)
        cacheDatasource.checkUriCalled(1)
    }

    @Test
    fun `test song deleting`() = runBlocking {
        val songId = 123L
        val result: HandleMediaResult = repository.deleteSong(songId)
        cacheDatasource.checkDeleteCalled(1)
    }

    private interface FakeCacheDatasource : PlaybackCacheDatasource {
        fun checkTagsCalled(times: Int)
        fun checkUriCalled(times: Int)
        fun checkDeleteCalled(times: Int)

        class Base : FakeCacheDatasource {
            private var tagsCalled = 0
            private var uriCalled = 0
            private var deleteSongCalled = 0

            override fun checkTagsCalled(times: Int) = assertEquals(times, tagsCalled)

            override fun checkUriCalled(times: Int) = assertEquals(times, uriCalled)

            override fun checkDeleteCalled(times: Int) = assertEquals(times, deleteSongCalled)

            override fun tags(id: Long): Flow<List<SongTag>> {
                tagsCalled++
                return flowOf()
            }

            override suspend fun uri(id: Long): String {
                uriCalled++
                return String()
            }

            override suspend fun deleteSong(songId: Long): HandleMediaResult {
                deleteSongCalled++
                return HandleMediaResult.AlreadyDeleted
            }
        }
    }

    private interface FakeMapper : SongTag.Mapper<TagPlaybackDomain> {
        class Base : FakeMapper {
            override fun map(
                id: Long,
                title: String,
                color: String,
                selected: Boolean
            ): TagPlaybackDomain = TagPlaybackDomain(title, color)
        }
    }
}