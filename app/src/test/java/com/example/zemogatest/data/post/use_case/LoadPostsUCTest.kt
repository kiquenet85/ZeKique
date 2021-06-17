package com.example.zemogatest.data.post.use_case

import com.example.zemogatest.data.db.entity.PostEntity
import com.example.zemogatest.data.db.entity.UserEntity
import com.example.zemogatest.data.post.repository.PostRepository
import com.example.zemogatest.data.user.repository.UserRepository
import com.example.zemogatest.presentation.post.list.PostUIState
import com.example.zemogatest.presentation.post.list.PostViewModel
import com.example.zemogatest.presentation.post.list.state.PostUI
import com.example.zemogatest.presentation.post.list.state.UserUI
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoadPostsUCTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    private lateinit var postRepository: PostRepository

    @MockK
    private lateinit var userRepository: UserRepository

    private lateinit var LoadPostsUC: LoadPostsUC

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)

        LoadPostsUC = LoadPostsUC(postRepository, userRepository)
    }

    @Test
    fun `when loadingPostsUC First 20 posts should be unreaded`() = runBlocking {
        //GIVEN
        val list = mutableListOf<PostEntity>()
        for (i in 0 until 30) {
            list.add(
                PostEntity(
                    "id ${i}",
                    "userID ${i}",
                    "title ${i}",
                    "This is just a nice number  ${i} post",
                    false,
                    seen = false
                )
            )
        }

        coEvery { postRepository.getAll(any()) } returns flow { emit(list) }
        coEvery { userRepository.getAll(any()) } returns flow { emit(listOf<UserEntity>()) }

        //WHEN
        val state = LoadPostsUC.execute(true, PostViewModel.Filter.NONE)

        //THEN
        val producedState = state.toList().first()
        assertTrue("State should be loaded", producedState is PostUIState.PostLoaded)
        assertEquals(
            "20 show as not seen items",
            20,
            (producedState as PostUIState.PostLoaded).value.filter { it.showAsNotSeen }.size
        )
        assertEquals(
            "others items as seen",
            10,
            producedState.value.filter { !it.showAsNotSeen }.size
        )
    }

    @Test
    fun `when loadingPostsUC should transform Entity to UI Model`() = runBlocking {
        //GIVEN
        val list = listOf(
            PostEntity(
                "id", "userID", "title", "This is just a nice number post",
                false, seen = false
            )
        )

        coEvery { postRepository.getAll(any()) } returns flow { emit(list) }
        coEvery { userRepository.getAll(any()) } returns flow { emit(listOf<UserEntity>()) }

        //WHEN
        val state = LoadPostsUC.execute(true, PostViewModel.Filter.NONE)

        //THEN
        val producedState = state.toList().first()
        val expectedUiModel =
            PostUI("id", "title", "This is just a nice number post", false, "userID", false, true)
        assertTrue("State should be loaded", producedState is PostUIState.PostLoaded)
        assertEquals(
            "transforming to UIModel",
            expectedUiModel,
            (producedState as PostUIState.PostLoaded).value.first()
        )
    }

    @Test
    fun `when loadingPostsUC should combine with user information`() = runBlocking {
        //GIVEN
        val list = listOf(
            PostEntity(
                "id", "userID", "title", "This is just a nice number post",
                false, seen = true
            )
        )

        val listUser = listOf(
            UserEntity(
                "userID",
                "Name",
                "some@email.com",
                "4656435435 4c534",
                "www.zemoga@nice.com"
            )
        )

        coEvery { postRepository.getAll(any()) } returns flow { emit(list) }
        coEvery { userRepository.getAll(any()) } returns flow { emit(listUser) }

        //WHEN
        val state = LoadPostsUC.execute(true, PostViewModel.Filter.NONE)

        //THEN
        val producedState = state.toList().first()
        val expectedUIUser =
            UserUI("Name", "some@email.com", "4656435435 4c534", "www.zemoga@nice.com")
        val expectedUIPost =
            PostUI(
                "id",
                "title",
                "This is just a nice number post",
                false,
                "userID",
                true,
                showAsNotSeen = false,
                userUI = expectedUIUser
            )
        assertTrue("State should be loaded", producedState is PostUIState.PostLoaded)
        assertEquals(
            "Combining with user information",
            expectedUIPost,
            (producedState as PostUIState.PostLoaded).value.first()
        )
    }

    @Test
    fun `when loadingPostsUC returns an empty list should return EMPTY STATE`() = runBlocking {
        //GIVEN
        val listUser = listOf(
            UserEntity(
                "userID",
                "Name",
                "some@email.com",
                "4656435435 4c534",
                "www.zemoga@nice.com"
            )
        )

        coEvery { postRepository.getAll(any()) } returns flow { emit(listOf<PostEntity>()) }
        coEvery { userRepository.getAll(any()) } returns flow { emit(listUser) }

        //WHEN
        val state = LoadPostsUC.execute(true, PostViewModel.Filter.NONE)

        //THEN
        val producedState = state.toList().first()
        println(producedState)
        assertTrue("State should be Empty", producedState is PostUIState.PostEmpty)
    }

    @Test
    fun `when loadingPostsUC with favorite filter, should only return favorite posts`() =
        runBlocking {
            //GIVEN

            val list = mutableListOf<PostEntity>()
            for (i in 0 until 30) {
                list.add(
                    PostEntity(
                        "id ${i}",
                        "userID ${i}",
                        "title ${i}",
                        "This is just a nice number  ${i} post",
                        i % 2 == 0,
                        false
                    )
                )
            }

            coEvery { postRepository.getAll(any()) } returns flow { emit(list) }
            coEvery { userRepository.getAll(any()) } returns flow { emit(listOf<UserEntity>()) }

            //WHEN
            val state = LoadPostsUC.execute(true, PostViewModel.Filter.FAVORITES)

            //THEN
            val producedState = state.toList().first()
            assertTrue("State should be loaded", producedState is PostUIState.PostLoaded)
            assertEquals(
                "Only favorite posts",
                15,
                (producedState as PostUIState.PostLoaded).value.size
            )
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }
}
