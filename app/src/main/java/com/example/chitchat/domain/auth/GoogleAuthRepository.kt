package com.example.chitchat.domain.auth

import android.util.Log
import com.example.chitchat.core.COLLECTION_USERS
import com.example.chitchat.core.SIGN_IN_REQUEST
import com.example.chitchat.core.SIGN_UP_REQUEST
import com.example.chitchat.domain.Response
import com.example.chitchat.models.User
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

// google
typealias OneTapSignInResponse = Response<BeginSignInResult> // we need BeginSignInResult to get credential
typealias SignInWithGoogleResponse = Response<Boolean>

interface GoogleAuthRepository {

    val isUserAuthenticatedWithFirebase: Boolean

    suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse

}

class GoogleAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    @Named(SIGN_IN_REQUEST)
    private val signInRequest: BeginSignInRequest,
    @Named(SIGN_UP_REQUEST)
    private val signUpRequest: BeginSignInRequest,
    private val db: FirebaseFirestore,
) : GoogleAuthRepository {

    override val isUserAuthenticatedWithFirebase: Boolean
        get() = auth.currentUser != null


    //displaying the One Tap sign-in
    override suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse {
        return try {
            Log.e("TAG", "oneTapSignInWithGoogle")
            val signInResult = oneTapClient.beginSignIn(signInRequest).await()
            Response.Success(signInResult)
        } catch (e: Exception) {
            try {
                Log.e("TAG", "oneTapSignInWithGoogle : NEW USER")
                val signUpResult = oneTapClient.beginSignIn(signUpRequest).await()
                Response.Success(signUpResult)
            } catch (e: Exception) {
                Log.e("TAG", "oneTapSignInWithGoogle : FAILED ${e.localizedMessage}")
                Response.Failure(e)
            }
        }
    }

    override suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse {
        return try {
            val authResult = auth.signInWithCredential(googleCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

            if (isNewUser) {
                Log.e("TAG", "firebaseSignInWithGoogle : NEW USER")
                addUserToDB()
            }
            Response.Success(true)
        } catch (e: Exception) {
            Log.e("TAG", "firebaseSignInWithGoogle Failed: ${e.localizedMessage}")
            Response.Failure(e)
        }
    }

    //---------------------
    //-- RealTime Database -
    //---------------------
    private suspend fun addUserToDB() {
        auth.currentUser?.apply {
            val user = User(
                id = uid,
                fullName = displayName ?: "Name Google",
                userImage = photoUrl.toString()
            )
            try {
                db.collection(COLLECTION_USERS).document(uid).set(user).await()
            }
            catch (e:Exception){
                Log.e("TAG","Google auth addUserToDB FAILED : ${e.localizedMessage} ")
            }
        }
    }

}

