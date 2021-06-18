package com.example.zemogatest.data.post.use_case

import com.example.zemogatest.data.db.entity.PostEntity
import com.example.zemogatest.data.post.repository.PostRepository
import com.example.zemogatest.presentation.post.list.ui_model.PostUI
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class UpdateSeenPostUCTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    private lateinit var postRepository: PostRepository

    private lateinit var updateSeenPostUCTest: UpdateSeenPostUC

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)

        updateSeenPostUCTest = UpdateSeenPostUC(postRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `when updating as SEEN should update corresponding property`() = runBlocking {
        //GIVEN
        val postUI = PostUI(
            "id", "title", "description",
            false, "userID", seen = false, showAsNotSeen = false
        )

        //WHEN
        updateSeenPostUCTest.execute(postUI)

        //THEN
        val expectedEntity = PostEntity(
            "id", "userID", "title",
            "description", favorite = false, seen = true
        )
        coVerify { postRepository.updateByID(expectedEntity) }
    }
}