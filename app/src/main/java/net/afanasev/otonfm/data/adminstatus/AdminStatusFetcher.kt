package net.afanasev.otonfm.data.adminstatus

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import net.afanasev.otonfm.log.Logger

private const val PATH = "admin_status/current"

class AdminStatusFetcher {

    private val reference = Firebase.database.getReference(PATH)

    fun observe(): Flow<AdminStatusModel?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val model = snapshot.getValue(AdminStatusModel::class.java)
                trySend(model)
            }

            override fun onCancelled(error: DatabaseError) {
                Logger.logAdminStatusError(error.message)
                trySend(null)
            }
        }

        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }
}
