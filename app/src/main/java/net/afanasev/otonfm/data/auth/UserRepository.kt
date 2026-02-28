package net.afanasev.otonfm.data.auth

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import net.afanasev.otonfm.log.Logger

private const val COLLECTION = "users"

class UserRepository {

    private val collection = Firebase.firestore.collection(COLLECTION)

    fun observeUser(uid: String): Flow<UserModel?> = callbackFlow {
        val registration = collection.document(uid).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Logger.logChatError("User observe error: ${error.message}")
                trySend(null)
                return@addSnapshotListener
            }
            val model = snapshot?.toObject(UserModel::class.java)
            trySend(model)
        }
        awaitClose { registration.remove() }
    }

    suspend fun userExists(uid: String): Boolean {
        return collection.document(uid).get().await().exists()
    }

    suspend fun createUser(uid: String, displayName: String, countryFlag: String) {
        val user = UserModel(
            displayName = displayName,
            countryFlag = countryFlag,
            createdAt = Timestamp.now(),
        )
        collection.document(uid).set(user).await()
    }
}
