package com.example.tagplayer.playback.domain

import android.content.IntentSender
import com.example.tagplayer.FakeHandleResponse
import com.example.tagplayer.playback.data.HandleMediaResult
import com.example.tagplayer.playback.presentation.TagPlaybackUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class PlaybackInteractorTest {
    private lateinit var interactor: PlaybackInteractor

    private lateinit var repository: FakeRepository
    private lateinit var handleResponse: FakeHandleResponse.Handle<SongDetailsResponse>
    private lateinit var responseMapper: FakeResponseMapper
    private lateinit var mapper: FakeMapper

    @Before
    fun setUp() {
        repository = FakeRepository.Base()
        handleResponse = FakeHandleResponse.Base()
        responseMapper = FakeResponseMapper.Base()
        mapper = FakeMapper.Base()
        interactor = PlaybackInteractor.Base(
            repository,
            handleResponse,
            responseMapper,
            mapper
        )
    }

    @Test
    fun `tags called`() {
        val songId = 123L
        val result = interactor.tags(songId)

        handleResponse.checkHandleCalled(1)
        repository.checkTagsCalled(1)
        assertEquals(result::class, SongDetailsResponse.TagsFlow::class)
    }

    @Test
    fun `share called`() = runBlocking {
        val songId = 123L
        val result = interactor.share(songId)

        handleResponse.checkHandleAsyncCalled(1)
        repository.checkUriCalled(1)
        assertEquals(result::class, SongDetailsResponse.SongUri::class)
    }

    @Test
    fun `already delete called`() = runBlocking {
        val songId = 123L
        repository.setDeleteResult(HandleMediaResult.AlreadyDeleted)
        val result = interactor.deleteSong(songId)

        handleResponse.checkHandleAsyncCalled(1)
        responseMapper.checkMapDeletionSucceedCalled(1)
        repository.checkDeleteCalled(1)
        assertEquals(result, SongDetailsResponse.DeletionSucceed)
    }

    @Test
    fun `delete with permission called`() = runBlocking {
        val songId = 123L
        val mockIntentSender: IntentSender = mock()
        repository.setDeleteResult(HandleMediaResult.DeletingWithPermission(mockIntentSender))
        val result = interactor.deleteSong(songId)

        handleResponse.checkHandleAsyncCalled(1)
        responseMapper.checkMapDeletingWithPermissionCalled(1)
        repository.checkDeleteCalled(1)
        assertEquals(result, SongDetailsResponse.DeletingWithPermission(mockIntentSender))
    }

    @Test
    fun `delete called throw error`() = runBlocking {
        val songId = 123L
        val error = "some error"
        repository.setDeleteResult(HandleMediaResult.Error(error))
        val result = interactor.deleteSong(songId)

        handleResponse.checkHandleAsyncCalled(1)
        responseMapper.checkMapErrorCalled(1)
        repository.checkDeleteCalled(1)
        assertEquals(result, SongDetailsResponse.Error(error))
    }

    private interface FakeRepository : PlaybackRepository<TagPlaybackDomain, HandleMediaResult> {
        fun checkTagsCalled(times: Int)
        fun checkUriCalled(times: Int)
        fun checkDeleteCalled(times: Int)
        fun setDeleteResult(result: HandleMediaResult)

        class Base : FakeRepository {
            private var deleteResult: HandleMediaResult = HandleMediaResult.AlreadyDeleted

            private var tagsCalled = 0
            private var uriCalled = 0
            private var deleteSongCalled = 0

            override fun checkTagsCalled(times: Int) = assertEquals(times, tagsCalled)

            override fun checkUriCalled(times: Int) = assertEquals(times, uriCalled)

            override fun checkDeleteCalled(times: Int) = assertEquals(times, deleteSongCalled)

            override fun setDeleteResult(result: HandleMediaResult) {
                deleteResult = result
            }

            override fun tags(id: Long): Flow<List<TagPlaybackDomain>> {
                tagsCalled++
                return flowOf()
            }

            override suspend fun uri(id: Long): String {
                uriCalled++
                return String()
            }

            override suspend fun deleteSong(songId: Long): HandleMediaResult {
                deleteSongCalled++
                return deleteResult
            }
        }
    }

    private interface FakeResponseMapper : HandleMediaResult.Mapper {
        fun checkMapDeletingWithPermissionCalled(times: Int)
        fun checkMapDeletionSucceedCalled(times: Int)
        fun checkMapErrorCalled(times: Int)

        class Base : FakeResponseMapper {
            private var mapDeletePermissionCalled = 0
            private var mapDeletionSucceedCalled = 0
            private var mapErrorCalled = 0

            override fun checkMapDeletingWithPermissionCalled(times: Int) =
                assertEquals(times, mapDeletePermissionCalled)

            override fun checkMapDeletionSucceedCalled(times: Int) =
                assertEquals(times, mapDeletionSucceedCalled)

            override fun checkMapErrorCalled(times: Int) =
                assertEquals(times, mapErrorCalled)

            override fun mapIntentSender(intentSender: IntentSender): SongDetailsResponse {
                mapDeletePermissionCalled++
                return SongDetailsResponse.DeletingWithPermission(intentSender)
            }

            override fun mapAlreadyDeleted(): SongDetailsResponse {
                mapDeletionSucceedCalled++
                return SongDetailsResponse.DeletionSucceed
            }

            override fun mapError(error: String): SongDetailsResponse {
                mapErrorCalled++
                return SongDetailsResponse.Error(error)
            }
        }
    }

    private interface FakeMapper : TagPlaybackDomain.Mapper<TagPlaybackUi> {
        class Base : FakeMapper {
            override fun map(title: String, color: String): TagPlaybackUi =
                TagPlaybackUi(title, color)
        }
    }
}