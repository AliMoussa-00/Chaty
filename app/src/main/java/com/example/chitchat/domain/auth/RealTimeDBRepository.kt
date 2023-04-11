package com.example.chitchat.domain.auth

import android.net.Uri
import android.util.Log
import com.example.chitchat.domain.Response
import com.example.chitchat.model.CurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

typealias AddUserToDBResponse = Response<Boolean>
typealias SetUserToDBResponse = Response<Boolean>
typealias UploadImageToStorageResponse = Response<Boolean>


interface RealTimeDBRepository {
    suspend fun addUserData(userData: CurrentUser): AddUserToDBResponse

    suspend fun setUserSpecificData(child: String, data: String): SetUserToDBResponse

    suspend fun uploadImageToStorage(image: String): UploadImageToStorageResponse
}

class RealTimeDBRepositoryImpl @Inject constructor(
    auth: FirebaseAuth,
    db: FirebaseDatabase,
    storage: FirebaseStorage,
) : RealTimeDBRepository {

    private val currentUserUid = auth.currentUser?.uid

    private val databaseReference = db.reference

    private val storageRef = storage.reference

    override suspend fun addUserData(userData: CurrentUser): AddUserToDBResponse {
        return try {
            databaseReference.child("Users").child(currentUserUid!!).setValue(userData).await()
            Response.Success(true)
        } catch (e: Exception) {
            Log.e("TAG", "addUserData RealTime Database FAILED: ${e.localizedMessage}")
            Response.Failure(e)
        }
    }

    override suspend fun setUserSpecificData(child: String, data: String): SetUserToDBResponse {
        return try {
            databaseReference.child("Users").child(currentUserUid!!).child(child).setValue(data)
                .await()
            Response.Success(true)
        } catch (e: Exception) {
            Log.e("TAG", "addUserData RealTime Database FAILED: ${e.localizedMessage}")
            Response.Failure(e)
        }
    }

    override suspend fun uploadImageToStorage(image: String): UploadImageToStorageResponse {
        return try {
            storageRef.child("images").child(currentUserUid!!).putFile(Uri.parse(image)).await()

            val uri = storageRef.child("images").child(currentUserUid!!).downloadUrl.await()

            setUserSpecificData("image", uri.toString())

            Response.Success(true)

        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

}

