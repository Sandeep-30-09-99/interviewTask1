package com.example.interviewtask.ui

import kotlinx.coroutines.*
import java.io.Closeable


data class AggregatedData(
    val user: UserEntity,
    val comments: List<CommentEntity>,
    val suggestedFriends: List<FriendEntity>
)

typealias UserId = String

data class UserEntity(val id: UserId, val name: String)

data class CommentEntity(val id: String, val content: String3)

data class FriendEntity(val id: String, val name: String)

class AggregateUserDataUseCase(
    private val resolveCurrentUser: suspend () -> UserEntity,
    private val fetchUserComments: suspend (UserId) -> List<CommentEntity>,
    private val fetchSuggestedFriends: suspend (UserId) -> List<FriendEntity>
) : Closeable {

    suspend fun aggregateDataForCurrentUser(): AggregatedData {
        return AggregatedData(
            getUser(),
            getUserComment(getUser().id),
            getSuggestedFriend(getUser().id)
        )
    }

    private lateinit var userJob: Deferred<UserEntity>
    private lateinit var commentJob: Deferred<List<CommentEntity>>
    private lateinit var suggestedFriendJob: Deferred<List<FriendEntity>>

    private suspend fun getUser(): UserEntity {
        val ceh = CoroutineExceptionHandler { _, e ->
            throw e
        }
        userJob = CoroutineScope(Dispatchers.IO + ceh).async {
            resolveCurrentUser.invoke()
        }
        return userJob.await()
    }

    private suspend fun getUserComment(userId: UserId): List<CommentEntity> {
        commentJob = CoroutineScope(Dispatchers.IO).async {
            fetchUserComments.invoke(userId)
        }
        return try {
            commentJob.await()
        } catch (e: Exception) {
            mutableListOf<CommentEntity>()
        }
    }

    private suspend fun getSuggestedFriend(userId: UserId): List<FriendEntity> {
        suggestedFriendJob = CoroutineScope(Dispatchers.IO).async {
            fetchSuggestedFriends.invoke(userId)
        }
        return try {
            suggestedFriendJob.await()
        } catch (e: Exception) {
            mutableListOf<FriendEntity>()
        }
    }


    override fun close() {
        userJob.cancel()
        suggestedFriendJob.cancel()
        commentJob.cancel()
    }
}