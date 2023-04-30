package com.example.chitchat.domain.firestore

import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import com.example.chitchat.core.COLLECTION_MESSAGES
import com.example.chitchat.core.COLLECTION_ROOMS
import com.example.chitchat.core.COLLECTION_USERS
import com.example.chitchat.core.FRIEND_ID
import com.example.chitchat.core.SENDER_ID
import com.example.chitchat.core.TEXT_MESSAGE
import com.example.chitchat.core.TIME_SENT
import com.example.chitchat.core.getRoomDocPath
import com.example.chitchat.domain.Response
import com.example.chitchat.models.Message
import com.example.chitchat.models.Room
import com.example.chitchat.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

typealias AddUserToDBResponse = Response<Boolean>
typealias SetUserToDBResponse = Response<Boolean>
typealias UploadImageToStorageResponse = Response<Boolean>

typealias SendMessageToFriendResponse = Response<Boolean>

interface FireStoreRepository {

    //----------------------------
    //------- Single User
    //----------------------------
    suspend fun addUserData(userData: User): AddUserToDBResponse

    suspend fun setUserSpecificData(fieldName: String, data: String): SetUserToDBResponse

    suspend fun uploadImageToStorage(image: String): UploadImageToStorageResponse

    //----------------------------
    //------- Multiple Users
    //----------------------------

    fun getAllUsers(): Flow<MutableList<User>>

    //----------------------------
    //------- Room & Messages
    //----------------------------

    suspend fun sendMessage(room: Room)

    fun getAllMessages(userID: String, friendId: String): Flow<MutableList<Message>>

}

class FireStoreRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    storage: FirebaseStorage,
) : FireStoreRepository {

    private val fireStoreUserReference = db.collection(COLLECTION_USERS)
    private val fireStoreRoomReference = db.collection(COLLECTION_ROOMS)

    private val storageRef = storage.reference

    override suspend fun addUserData(userData: User): AddUserToDBResponse {
        return try {

            fireStoreUserReference.document(auth.currentUser?.uid!!).set(userData).await()

            Response.Success(true)
        } catch (e: Exception) {
            Log.e("TAG", "addUserData RealTime Database FAILED: ${e.localizedMessage}")
            Response.Failure(e)
        }
    }

    override suspend fun setUserSpecificData(fieldName: String, data: String): SetUserToDBResponse {
        return try {

            Log.e("TAG", "addUserData uid = ${auth.currentUser?.uid}")
            fireStoreUserReference.document(auth.currentUser?.uid!!).update(fieldName, data).await()

            Response.Success(true)
        } catch (e: Exception) {
            Log.e("TAG", "addUserData RealTime Database FAILED: ${e.localizedMessage}")
            Response.Failure(e)
        }
    }

    override suspend fun uploadImageToStorage(image: String): UploadImageToStorageResponse {
        return try {

            storageRef.child("images").child(auth.currentUser?.uid!!).putFile(Uri.parse(image))
                .await()

            val uri = storageRef.child("images").child(auth.currentUser?.uid!!).downloadUrl.await()

            setUserSpecificData("userImage", uri.toString())

        } catch (e: Exception) {
            Response.Failure(e)
        }
    }


    //----------------------------
    //------- Multiple User
    //----------------------------

    override fun getAllUsers(): Flow<MutableList<User>> {
        return fireStoreUserReference.snapshots().map {
            it.toObjects(User::class.java)
        }
    }

    //----------------------------
    //------- Room & Messages
    //----------------------------
    override suspend fun sendMessage(room: Room) {

        val myRoom = mapOf(
            SENDER_ID to room.userId,
            FRIEND_ID to room.friendId
        )
        val myMessage = mapOf(
            SENDER_ID to room.userId,
            TIME_SENT to Calendar.getInstance().time.toString(),
            TEXT_MESSAGE to room.message.text
        )

        val roomRef =
            fireStoreRoomReference.document(getRoomDocPath(room.userId, room.friendId)).get()
                .await()
        if (roomRef.exists()) {
            roomRef.reference.collection(COLLECTION_MESSAGES).add(myMessage)
        } else {
            fireStoreRoomReference
                .document(getRoomDocPath(room.userId, room.friendId)).set(myRoom).await()
            fireStoreRoomReference
                .document(getRoomDocPath(room.userId, room.friendId))
                .collection(COLLECTION_MESSAGES)
                .add(myMessage).await()
        }
    }

    override fun getAllMessages(
        userID: String,
        friendId: String,
    ): Flow<MutableList<Message>> {

        return fireStoreRoomReference.document(getRoomDocPath(userID, friendId))
            .collection(COLLECTION_MESSAGES)
            .orderBy(TIME_SENT,Query.Direction.DESCENDING)
            .snapshots()
            .map {
                it.toObjects(Message::class.java)
            }

    }


}


