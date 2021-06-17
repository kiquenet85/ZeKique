package com.example.zemogatest.data.post.repository

import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.data.db.entity.PostEntity
import com.example.zemogatest.data.post.sources.PostLocalSource
import com.example.zemogatest.data.post.sources.PostRemoteSource
import com.example.zemogatest.domain.remote.PostResponseDTO
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

class PostRepositoryTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private var postRepository: PostRepository? = null

    @MockK
    private lateinit var localSource: PostLocalSource

    @MockK
    private lateinit var remoteSource: PostRemoteSource

    @MockK
    private lateinit var errorHandler: ErrorHandler

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)
        postRepository = PostRepository(localSource, remoteSource, errorHandler)

        coEvery { localSource.getAll() } returns listOf()
    }

    @Test
    fun `Getting all posts should call the database and remote endpoint`() = runBlocking {
        //GIVEN
        every { localSource.getAllAndObserve() } returns flow {
            emit(listOf(postEntity))
        }

        coEvery { remoteSource.getAll() } returns listOf(postResponseDTO)
        coEvery { localSource.updateAll(any()) } returns true

        //WHEN
        postRepository?.getAll(GetPostInfo())?.collect()

        //THEN
        verify { localSource.getAllAndObserve() }
        coVerify { remoteSource.getAll() }
        verify(exactly = 0) { errorHandler.report(any()) }
    }

    @Test
    fun `Getting all posts should receive emission from local and remote sources`() = runBlocking {
        //GIVEN
        every { localSource.getAllAndObserve() } returns flow {
            emit(listOf(postEntity))
        }

        coEvery { remoteSource.getAll() } returns listOf(postResponseDTO)

        //WHEN
        val firstDatabaseEmission = postRepository?.getAll(GetPostInfo())?.toList()?.first()

        //THEN
        assertEquals("Emission database", postEntity, firstDatabaseEmission?.first())

        val expectedSecondDBEvent =
            listOf(expectedPostEntity)
        coVerify { localSource.updateAll(expectedSecondDBEvent) }
    }

    @Test
    fun `Getting all should not call remote source whenever isRemote is FALSE`() = runBlocking {
        //GIVEN
        every { localSource.getAllAndObserve() } returns flow {
            emit(listOf(postEntity))
        }

        //WHEN
        postRepository?.getAll(GetPostInfo(requiresRemote = false))?.collect()

        //THEN
        verify { localSource.getAllAndObserve() }
        coVerify(exactly = 0) { remoteSource.getAll() }
    }

    @Test
    fun `Getting all posts remote call error should report the error but keep the database emission`() =
        runBlocking {
            //GIVEN
            every { localSource.getAllAndObserve() } returns flow {
                emit(listOf(postEntity))
            }

            //WHEN
            val firstDatabaseEmission = postRepository?.getAll(GetPostInfo())?.toList()?.first()

            //THEN
            assertEquals("Emission database", postEntity, firstDatabaseEmission?.first())
            verify { localSource.getAllAndObserve() }
            coVerify { remoteSource.getAll() }
            verify { errorHandler.report(any()) }
        }

    @Test(expected = IllegalStateException::class)
    fun `Getting all posts local call error should throws an error`() {
        //GIVEN
        every { localSource.getAllAndObserve() } throws java.lang.IllegalStateException()

        //WHEN
        runBlocking {
            postRepository?.getAll(GetPostInfo())?.collect()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    companion object {
        val postEntity = PostEntity("id", "userID", "title", "This is just a long post", false)
        val postResponseDTO =
            PostResponseDTO("id2", "userID2", "title2", "This is just another nice post")
        val expectedPostEntity =
            PostEntity("id2", "userID2", "title2", "This is just another nice post", false)
    }
}