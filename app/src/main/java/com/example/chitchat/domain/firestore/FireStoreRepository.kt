package com.example.chitchat.domain.firestore

import android.net.Uri
import android.util.Log
import com.example.chitchat.core.COLLECTION_USERS
import com.example.chitchat.domain.Response
import com.example.chitchat.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

typealias AddUserToDBResponse = Response<Boolean>
typealias SetUserToDBResponse = Response<Boolean>
typealias UploadImageToStorageResponse = Response<Boolean>

typealias GetAllUsersDocResponse= Response<List<DocumentSnapshot>>


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

}

class FireStoreRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    db: FirebaseFirestore,
    storage: FirebaseStorage,
) : FireStoreRepository {

    private val fireStoreReference = db.collection(COLLECTION_USERS)

    private val storageRef = storage.reference

    override suspend fun addUserData(userData: User): AddUserToDBResponse {
        return try {
            Log.e("TAG","addUserData uid = ${auth.currentUser?.uid}")
            fireStoreReference.document(auth.currentUser?.uid!!).set(userData).await()

            Response.Success(true)
        } catch (e: Exception) {
            Log.e("TAG", "addUserData RealTime Database FAILED: ${e.localizedMessage}")
            Response.Failure(e)
        }
    }

    override suspend fun setUserSpecificData(fieldName: String, data: String): SetUserToDBResponse {
        return try {

            Log.e("TAG","addUserData uid = ${auth.currentUser?.uid}")
            fireStoreReference.document(auth.currentUser?.uid!!).update(fieldName,data).await()

            Response.Success(true)
        } catch (e: Exception) {
            Log.e("TAG", "addUserData RealTime Database FAILED: ${e.localizedMessage}")
            Response.Failure(e)
        }
    }

    override suspend fun uploadImageToStorage(image: String): UploadImageToStorageResponse {
        return try {

            storageRef.child("images").child(auth.currentUser?.uid!!).putFile(Uri.parse(image)).await()

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
        return fireStoreReference.snapshots().map {
            it.toObjects(User::class.java)
        }
    }

}

