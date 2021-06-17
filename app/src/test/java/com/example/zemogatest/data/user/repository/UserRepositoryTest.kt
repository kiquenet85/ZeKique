package com.example.zemogatest.data.user.repository

import com.example.zemogatest.common.error.ErrorHandler
import com.example.zemogatest.data.db.entity.UserEntity
import com.example.zemogatest.data.user.sources.UserLocalSource
import com.example.zemogatest.data.user.sources.UserRemoteSource
import com.example.zemogatest.domain.remote.UserResponseDTO
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

class UserRepositoryTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private var userRepository: UserRepository? = null

    @MockK
    private lateinit var localSource: UserLocalSource

    @MockK
    private lateinit var remoteSource: UserRemoteSource

    @MockK
    private lateinit var errorHandler: ErrorHandler

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)
        userRepository = UserRepository(localSource, remoteSource, errorHandler)
    }

    @Test
    fun `Getting all users should call the database and remote endpoint`() = runBlocking {
        //GIVEN
        every { localSource.getAll() } returns flow {
            emit(listOf(userEntity))
        }

        coEvery { remoteSource.getAll() } returns listOf(userResponseDTO)
        coEvery { localSource.updateAll(any()) } returns true

        //WHEN
        userRepository?.getAll(GetUserInfo())?.collect()

        //THEN
        verify { localSource.getAll() }
        coVerify { remoteSource.getAll() }
        verify(exactly = 0) { errorHandler.report(any()) }
    }

    @Test
    fun `Getting all users should receive emission from local and remote sources`() = runBlocking {
        //GIVEN
        every { localSource.getAll() } returns flow {
            emit(listOf(userEntity))
        }

        coEvery { remoteSource.getAll() } returns listOf(userResponseDTO)

        //WHEN
        val firstDatabaseEmission = userRepository?.getAll(GetUserInfo())?.toList()?.first()

        //THEN
        assertEquals("Emission database", userEntity, firstDatabaseEmission?.first())

        val expectedSecondDBEvent =
            listOf(UserEntity("id2", "name2", "email2", "100002", "website2"))
        coVerify { localSource.updateAll(expectedSecondDBEvent) }
    }

    @Test
    fun `Getting all should not call remote source whenever isRemote is FALSE`() = runBlocking {
        //GIVEN
        every { localSource.getAll() } returns flow {
            emit(listOf(userEntity))
        }

        //WHEN
        userRepository?.getAll(GetUserInfo(requiresRemote = false))?.collect()

        //THEN
        verify { localSource.getAll() }
        coVerify(exactly = 0) { remoteSource.getAll() }
    }

    @Test
    fun `Getting all users remote call error should report the error but keep the database emission`() =
        runBlocking {
            //GIVEN
            every { localSource.getAll() } returns flow {
                emit(listOf(userEntity))
            }

            //WHEN
            val firstDatabaseEmission = userRepository?.getAll(GetUserInfo())?.toList()?.first()

            //THEN
            assertEquals("Emission database", userEntity, firstDatabaseEmission?.first())
            verify { localSource.getAll() }
            coVerify { remoteSource.getAll() }
            verify { errorHandler.report(any()) }
        }

    @Test(expected = IllegalStateException::class)
    fun `Getting all users local call error should throws an error`() {
        //GIVEN
        every { localSource.getAll() } throws java.lang.IllegalStateException()

        //WHEN
        runBlocking {
            userRepository?.getAll(GetUserInfo())?.collect()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    companion object {
        val userEntity = UserEntity("id", "name", "email", "981729872", "website")
        val userResponseDTO =
            UserResponseDTO("id2", "name2", "user_name2", "email2", "100002", "website2")
    }
}