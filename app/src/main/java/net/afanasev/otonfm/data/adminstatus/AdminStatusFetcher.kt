package net.afanasev.otonfm.data.adminstatus

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import net.afanasev.otonfm.log.Logger

private const val COLLECTION = "admin_status"
private const val DOCUMENT = "current"

class AdminStatusFetcher {

    private val docRef = Firebase.firestore.collection(COLLECTION).document(DOCUMENT)

    fun observe(): Flow<AdminStatusModel?> = callbackFlow {
        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Logger.logAdminStatusError(error.message ?: "Unknown Firestore error")
                trySend(null)
                return@addSnapshotListener
            }

            val model = snapshot?.toObject(AdminStatusModel::class.java)
            trySend(model)
        }

        awaitClose { registration.remove() }
    }
}
