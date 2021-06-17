package com.example.zemogatest.data.comment.repository

import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.data.comment.sources.CommentLocalSource
import com.example.zemogatest.data.comment.sources.CommentRemoteSource
import com.example.zemogatest.data.db.entity.CommentEntity
import com.example.zemogatest.domain.remote.CommentResponseDTO
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class CommentRepositoryTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private var commentRepository: CommentRepository? = null

    @MockK
    private lateinit var localSource: CommentLocalSource

    @MockK
    private lateinit var remoteSource: CommentRemoteSource

    @MockK
    private lateinit var errorHandler: ErrorHandler

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)
        commentRepository = CommentRepository(localSource, remoteSource, errorHandler)
    }

    @Test
    fun `Getting all comments should call the database and remote endpoint`() = runBlocking {
        //GIVEN
        every { localSource.getAll() } returns flow {
            emit(listOf(commentEntity))
        }

        coEvery { remoteSource.getAll() } returns listOf(commentResponseDTO)
        coEvery { localSource.updateAll(any()) } returns true

        //WHEN
        commentRepository?.getAll(GetCommentInfo())?.collect()

        //THEN
        verify { localSource.getAll() }
        coVerify { remoteSource.getAll() }
        verify(exactly = 0) { errorHandler.report(any()) }
    }

    @Test
    fun `Getting all comments should receive emission from local and remote sources`() =
        runBlocking {
            //GIVEN
            every { localSource.getAll() } returns flow {
                emit(listOf(commentEntity))
            }

            coEvery { remoteSource.getAll() } returns listOf(commentResponseDTO)

            //WHEN
            val firstDatabaseEmission =
                commentRepository?.getAll(GetCommentInfo())?.toList()?.first()

            //THEN
            assertEquals("Emission database", commentEntity, firstDatabaseEmission?.first())

            val expectedSecondDBEvent =
                listOf(expectedCommentEntity)
            coVerify { localSource.updateAll(expectedSecondDBEvent) }
        }

    @Test
    fun `Getting all should not call remote source whenever isRemote is FALSE`() = runBlocking {

        //GIVEN
        every { localSource.getAll() } returns flow {
            emit(listOf(commentEntity))
        }

        //WHEN
        commentRepository?.getAll(GetCommentInfo(requiresRemote = false))?.collect()

        //THEN
        verify { localSource.getAll() }
        coVerify(exactly = 0) { remoteSource.getAll() }
    }

    @Test
    fun `Getting all comments remote call error should report the error but keep the database emission`() =
        runBlocking {
            //GIVEN
            every { localSource.getAll() } returns flow {
                emit(listOf(commentEntity))
            }

            //WHEN
            val firstDatabaseEmission =
                commentRepository?.getAll(GetCommentInfo())?.toList()?.first()

            //THEN
            assertEquals("Emission database", commentEntity, firstDatabaseEmission?.first())
            verify { localSource.getAll() }
            coVerify { remoteSource.getAll() }
            verify { errorHandler.report(any()) }
        }

    @Test(expected = IllegalStateException::class)
    fun `Getting all comments local call error should throws an error`() {
        //GIVEN
        every { localSource.getAll() } throws java.lang.IllegalStateException()

        //WHEN
        runBlocking {
            commentRepository?.getAll(GetCommentInfo())?.toList()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    companion object {
        val commentEntity =
            CommentEntity("id", "userID", "postID", "CommentName", "This is a nice comment")
        val commentResponseDTO =
            CommentResponseDTO(
                "id2",
                "postID2",
                "someName",
                "some@email.com",
                "This is a bad comment."
            )
        val expectedCommentEntity =
            CommentEntity("id2", "some@email.com", "postID2", "someName", "This is a bad comment.")
    }
}