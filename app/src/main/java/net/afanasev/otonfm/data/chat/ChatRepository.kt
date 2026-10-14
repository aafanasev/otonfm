package net.afanasev.otonfm.data.chat

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import net.afanasev.otonfm.data.auth.UserModel
import net.afanasev.otonfm.data.auth.USERS_COLLECTION
import net.afanasev.otonfm.util.log.Logger

private const val COLLECTION = "messages"
private const val MESSAGE_LIMIT = 50L

class ChatRepository {

    private val collection = Firebase.firestore.collection(COLLECTION)

    fun observeMessages(): Flow<List<MessageModel>> = callbackFlow {
        val registration = collection
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .limitToLast(MESSAGE_LIMIT)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Logger.logChatError("Messages observe error: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(MessageModel::class.java)?.copy(id = doc.id)
                }?.filter { !it.isHidden } ?: emptyList()

                trySend(messages)
            }

        awaitClose { registration.remove() }
    }

    /**
     * Observes the most recently pinned message, independently of [observeMessages],
     * so it stays available after more than [MESSAGE_LIMIT] newer messages arrive.
     *
     * Requires a Firestore composite index on `messages`: `isPinned` ascending,
     * `createdAt` descending.
     *
     * @return a flow emitting the latest pinned message, or `null` when there is none,
     * when it is hidden, or when the query fails (the error is logged).
     */
    fun observePinnedMessage(): Flow<MessageModel?> = callbackFlow {
        val registration = collection
            .whereEqualTo("isPinned", true)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Logger.logChatError("Pinned message observe error: ${error.message}")
                    trySend(null)
                    return@addSnapshotListener
                }

                // isHidden is checked here rather than in the query because older
                // documents may lack the field, and isHidden == false would skip them.
                val pinnedMessage = snapshot?.documents?.firstOrNull()
                    ?.let { doc -> doc.toObject(MessageModel::class.java)?.copy(id = doc.id) }
                    ?.takeUnless { it.isHidden }

                trySend(pinnedMessage)
            }

        awaitClose { registration.remove() }
    }

    suspend fun sendMessage(text: String, authorId: String, user: UserModel) {
        val data = hashMapOf(
            "type" to MessageType.USER_MESSAGE.value,
            "text" to text,
            "authorId" to authorId,
            "authorName" to user.displayName,
            "authorFlag" to user.countryFlag,
            "authorIsAdmin" to user.isAdmin,
            "authorIsPremium" to user.isPremium,
            "platform" to "android",
            "createdAt" to FieldValue.serverTimestamp(),
        )
        collection.add(data).await()

        // Fire-and-forget: updating lastMessageAt is non-critical to the chat flow,
        // so we intentionally don't await it — a failure here is acceptable.
        Firebase.firestore.collection(USERS_COLLECTION).document(authorId)
            .update("lastMessageAt", Timestamp.now())
    }
}
