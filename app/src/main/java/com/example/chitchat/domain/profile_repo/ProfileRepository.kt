package com.example.chitchat.domain.profile_repo

import com.example.chitchat.domain.Response
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

typealias SignOutResponse = Response<Boolean>

interface ProfileRepository {

    suspend fun signOut(): SignOutResponse
}

class ProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): ProfileRepository{


    override suspend fun signOut(): SignOutResponse {
        return try {

            auth.signOut()
            Response.Success(true)
        }
        catch (e:Exception){
            Response.Failure(e)
        }
    }
}