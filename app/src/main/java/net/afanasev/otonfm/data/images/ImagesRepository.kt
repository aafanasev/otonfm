package net.afanasev.otonfm.data.images

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

private const val COLLECTION = "images"

class ImagesRepository {

    private val collectionRef = Firebase.firestore.collection(COLLECTION)

    suspend fun getRandomImageUrl(): String? {
        val snapshot = collectionRef.get().await()
        if (snapshot.isEmpty) return null
        val doc = snapshot.documents.random()
        return doc.toObject(ImageModel::class.java)?.url?.takeIf { it.isNotBlank() }
    }
}
