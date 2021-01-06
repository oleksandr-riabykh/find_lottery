package com.limestudio.findlottery.data.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.kiwimob.firestore.coroutines.await
import com.limestudio.findlottery.data.models.Draw
import com.limestudio.findlottery.data.models.Ticket
import com.limestudio.findlottery.data.models.User
import com.limestudio.findlottery.extensions.toDateFormat
import com.limestudio.findlottery.presentation.ui.auth.signup.ImageModel


const val TABLE_USERS = "users"
const val TABLE_TICKETS = "tickets"
const val TABLE_DRAWS = "draws"

class FirebaseManager(applicationContext: Context?) {
    private val database = Firebase.firestore


    fun getDatabase() = database

    fun getDrawsByDate(date: String) =
        database.collection(TABLE_DRAWS).whereEqualTo("date", date)
            .get()

//    suspend fun getDrawsByDateAsync(date: String): MutableList<Draw> =
//        database.collection(TABLE_DRAWS).whereEqualTo("date", date.toDateFormat())
//            .whereEqualTo("userId", Firebase.auth.currentUser?.uid)
//            .get().await().toObjects(Draw::class.java)

    suspend fun getDrawsByDateAsync(date: String): MutableList<Draw> =
        Firebase.auth.currentUser?.uid?.let {
            database.collection(TABLE_DRAWS).whereEqualTo("date", date.toDateFormat())
                .whereEqualTo("userId", it)
                .get().await().toObjects(Draw::class.java)
        } ?: mutableListOf()

    suspend fun getDraws(userId: String): List<Draw> =
        database.collection(TABLE_DRAWS).whereEqualTo("userId", userId)
            .get().await().toObjects(Draw::class.java)


    suspend fun getTickets(drawId: String): List<Ticket> =
        database.collection(TABLE_TICKETS).whereEqualTo("drawId", drawId)
            .whereEqualTo("userId", Firebase.auth.currentUser?.uid)
            .get()
            .await()
            .toObjects(Ticket::class.java)

    suspend fun getUserTicketsCount(): Int =
        database.collection(TABLE_TICKETS)
            .whereEqualTo("userId", Firebase.auth.currentUser?.uid)
            .get()
            .await()
            .toObjects(Ticket::class.java).size

    fun addTicket(ticket: Ticket) {
        database.collection(TABLE_TICKETS).document(ticket.id).set(ticket.toMap())
            .addOnSuccessListener { documentReference ->
                Log.d("TAG_database", "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w("TAG_database", "Error adding document", e)
            }
    }

    fun addDraw(draw: Draw) {
        database.collection(TABLE_DRAWS).document(draw.id).set(draw.toMap())
            .addOnSuccessListener { documentReference ->
                Log.d("TAG_database", "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.w("TAG_database", "Error adding document", e)
            }
    }

    fun uploadImage(mFileName: String, fileModel: ImageModel): String {
        val storageRef = FirebaseStorage.getInstance().reference
        val existingUrl = "images/${fileModel.folderName}/${mFileName}.jpg"
        val mountainImagesRef = storageRef.child(existingUrl)
        fileModel.file.let { uri ->
            mountainImagesRef.putFile(uri).addOnFailureListener {
                throw Exception("Fail to upload the image")
            }
        }
        return existingUrl
    }

    suspend fun addUser(user: User): Void =
        database.collection(TABLE_USERS).document(user.id).set(user.toMap()).await()


    suspend fun deleteDraw(draw: Draw) {
        database.collection(TABLE_DRAWS).document(draw.id).delete().await()
        getTickets(draw.id).forEach { deleteTicket(it) }
    }

    suspend fun deleteTicket(ticket: Ticket) {
        database.collection(TABLE_TICKETS).document(ticket.id).delete().await()
    }

    companion object : SingletonHolder<FirebaseManager, Context?>(::FirebaseManager)
}

