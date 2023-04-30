package com.example.chitchat.domain.auth

import android.app.Activity
import android.util.Log
import com.example.chitchat.domain.Response
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import javax.inject.Inject


typealias SendVerificationCodeResponse = Response<Boolean>
typealias ResendVerificationCodeResponse = Response<Boolean>
typealias SignInWithPhoneCredentialResponse = Response<Boolean>

interface PhoneAuthRepository {

    suspend fun sendVerificationCode(phone:String,activity: Activity) : SendVerificationCodeResponse

    suspend fun resendCode(): ResendVerificationCodeResponse

    suspend fun signInWithPhoneCredential(otpCode: String): SignInWithPhoneCredentialResponse

}

class PhoneAuthRepositoryImpl @Inject constructor (
    private val auth: FirebaseAuth
): PhoneAuthRepository{

    private lateinit var activity: Activity
    private lateinit var phoneNum: String
    private lateinit var verificationCode: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override suspend fun sendVerificationCode(phone: String, myactivity: Activity): SendVerificationCodeResponse {
        activity = myactivity
        phoneNum = phone
        return try{
            val options = activity.let {
                PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phone)
                    .setTimeout(10L,TimeUnit.SECONDS)
                    .setActivity(it)
                    .setCallbacks(callBacks)
                    .build()
            }
            if (options != null) {
                PhoneAuthProvider.verifyPhoneNumber(options)
                return Response.Success(true)
            }
            else{
                return Response.Success(false)
            }
        }
        catch (e:Exception){
            Log.e("TAG","sendVerificationCode EXCEPTION: ${e.localizedMessage}")
            Response.Failure(e)
        }
    }

    override suspend fun resendCode(): ResendVerificationCodeResponse {
        return try{
            val options = activity.let {
                PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNum)
                    .setTimeout(10L,TimeUnit.SECONDS)
                    .setActivity(it)
                    .setCallbacks(callBacks)
                    .setForceResendingToken(resendToken)
                    .build()
            }
            if (options != null) {
                PhoneAuthProvider.verifyPhoneNumber(options)
                return Response.Success(true)
            }
            else{
                return Response.Success(false)
            }
        }
        catch (e:Exception){
            Log.e("TAG","sendVerificationCode EXCEPTION: ${e.localizedMessage}")
            Response.Failure(e)
        }
    }

    override suspend fun signInWithPhoneCredential(otpCode:String): SignInWithPhoneCredentialResponse {
        val credential = PhoneAuthProvider.getCredential(verificationCode,otpCode)
        return try {
            auth.signInWithCredential(credential).await()
            Response.Success(true)
        }
        catch (e:Exception){
            Log.e("TAG","signInWithPhoneCredential EXCEPTION: ${e.localizedMessage}")
            Response.Failure(e)
        }
    }


    private val callBacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {}

        override fun onVerificationFailed(p0: FirebaseException) {
           Log.e("TAG","callBacks FAILED : $p0")
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)

            verificationCode = p0
            resendToken = p1
        }
    }

}