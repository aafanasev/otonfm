package net.afanasev.otonfm.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

private const val WEB_CLIENT_ID =
    "506038865559-nngear1povfic4djb60joa087e7de2t4.apps.googleusercontent.com"

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser? get() = auth.currentUser

    fun observeAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    suspend fun signInWithGoogle(context: Context): Result<FirebaseUser> = runCatching {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(context, request)
        val googleIdToken = GoogleIdTokenCredential.createFrom(result.credential.data)

        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken.idToken, null)
        val authResult = auth.signInWithCredential(firebaseCredential).await()
        authResult.user!!
    }

    fun signOut() {
        auth.signOut()
    }
}
