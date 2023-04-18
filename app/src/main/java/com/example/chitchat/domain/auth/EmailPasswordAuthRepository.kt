package com.example.chitchat.domain.auth

import com.example.chitchat.domain.Response
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


typealias SignUpEmailPassResponse = Response<Boolean>
typealias SendVerificationEmailResponse = Response<Boolean>
typealias SignInEmailPassResponse = Response<Boolean>
typealias SendPasswordResetEmailResponse = Response<Boolean>
typealias isEmailVerifiedResponse = Boolean

interface EmailPasswordAuthRepository {

    suspend fun firebaseSignUpWithEmailAndPassword(
        email: String,
        password: String,
    ): SignUpEmailPassResponse

    suspend fun sendEmailVerification(): SendVerificationEmailResponse

    suspend fun firebaseSignInWithEmailPassword(
        email: String,
        password: String,
    ): SignInEmailPassResponse

    suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse

    fun isEmailVerified(): isEmailVerifiedResponse
}

class EmailPasswordAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : EmailPasswordAuthRepository {

    override suspend fun firebaseSignUpWithEmailAndPassword(
        email: String,
        password: String,
    ): SignUpEmailPassResponse {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendEmailVerification(): SendVerificationEmailResponse {
        return try {
            auth.currentUser?.sendEmailVerification()?.await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun firebaseSignInWithEmailPassword(
        email: String,
        password: String,
    ): SignInEmailPassResponse {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    override fun isEmailVerified(): isEmailVerifiedResponse {
        return auth.currentUser?.isEmailVerified ?: false
    }
}