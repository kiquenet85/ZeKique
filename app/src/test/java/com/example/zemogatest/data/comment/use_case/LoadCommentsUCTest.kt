package com.example.zemogatest.data.comment.use_case

import com.example.zemogatest.data.comment.repository.CommentRepository
import com.example.zemogatest.data.db.entity.CommentEntity
import com.example.zemogatest.presentation.post.detail.CommentUIState
import com.example.zemogatest.presentation.post.list.ui_model.PostUI
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

class LoadCommentsUCTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    private lateinit var commentRepository: CommentRepository

    private lateinit var loadCommentsUC: LoadCommentsUC

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(mainThreadSurrogate)

        loadCommentsUC = LoadCommentsUC(commentRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `when loading comments and returning an empty list should return EMPTY state`() =
        runBlocking {
            //GIVEN
            coEvery { commentRepository.getAll(any()) } returns flow { emit(listOf<CommentEntity>()) }

            //WHEN
            val producedState = loadCommentsUC.execute(true, postUI).toList().first()

            //THEN
            assertEquals("state should be CommentEmpty", producedState, CommentUIState.CommentEmpty)
        }

    @Test
    fun `when loading comments should only emit related comments`() = runBlocking {
        //GIVEN

        coEvery { commentRepository.getAll(any()) } returns flow { emit(comments) }

        //WHEN
        val producedState = loadCommentsUC.execute(true, postUI).toList().first()

        //THEN
        assertTrue("state should be CommentLoaded", producedState is CommentUIState.CommentLoaded)
        assertEquals(
            "state should be CommentLoaded", 2,
            (producedState as CommentUIState.CommentLoaded).value.size
        )
    }

    companion object {
        val postUI = PostUI(
            "id", "title", "description",
            false, "userID", seen = false, showAsNotSeen = false
        )

        val comments = listOf(
            CommentEntity("id", "userID", "id", "name", "body"),
            CommentEntity("id", "userID", "id2", "name", "body"),
            CommentEntity("id", "userID", "id2", "name", "body"),
            CommentEntity("id", "userID", "id", "name", "body")
        )
    }
}