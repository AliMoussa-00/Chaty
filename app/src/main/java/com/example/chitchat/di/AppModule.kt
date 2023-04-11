package com.example.chitchat.di

import android.app.Application
import android.content.Context
import com.example.chitchat.R
import com.example.chitchat.core.SIGN_IN_REQUEST
import com.example.chitchat.core.SIGN_UP_REQUEST
import com.example.chitchat.domain.auth.*
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideRealTimeDB() = Firebase.database

    @Provides
    fun provideStorageReference() = Firebase.storage

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        @Named(SIGN_IN_REQUEST)
        signInRequest: BeginSignInRequest,
        @Named(SIGN_UP_REQUEST)
        signUpRequest: BeginSignInRequest,
        db: FirebaseDatabase,
    ): GoogleAuthRepository = GoogleAuthRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInRequest = signInRequest,
        signUpRequest = signUpRequest,
        db = db
    )

    //------------------------------
    // Google authentication
    //------------------------------
    @Provides
    fun provideOneTapClient(
        @ApplicationContext
        context: Context,
    ) = Identity.getSignInClient(context)

    @Provides
    @Named(SIGN_IN_REQUEST)
    fun provideSignInRequest(
        app: Application,
    ) = BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.Builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(true)    // show us only the accounts previously used to sign in
                .build()
        )
        .setAutoSelectEnabled(true)     // automatically sign in when exactly one credential is retrieved.
        .build()

    @Provides
    @Named(SIGN_UP_REQUEST)
    fun provideSignUpRequest(
        app: Application,
    ) = BeginSignInRequest.Builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.Builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false)    // show us all available accounts that exist on the device.
                .build()
        )
        .build()

    // old google methode
    /*@Provides
    fun provideGoogleSignInOptions(
        app: Application
    )= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()*/

    /* @Provides
     fun providesGoogleSignInClient(
         app: Application,
         options: GoogleSignInOptions
     ) = GoogleSignIn.getClient(app,options)*/

    //---------------------------------
    // Email & Password Authentication
    //---------------------------------
    @Provides
    @Singleton
    fun provideEmailPasswordAuthRepository(auth: FirebaseAuth): EmailPasswordAuthRepository =
        EmailPasswordAuthRepositoryImpl(auth = auth)

    //---------------------------------
    // Phone Authentication
    //---------------------------------
    @Provides
    @Singleton
    fun providePhoneAuthRepository(
        auth: FirebaseAuth,
    ): PhoneAuthRepository {
        return PhoneAuthRepositoryImpl(auth)
    }

    //---------------------------------
    // profile: RealTileDB & Storage
    //---------------------------------
    @Provides
    @Singleton
    fun provideProfileRepository(
        auth: FirebaseAuth,
        db: FirebaseDatabase,
        storage: FirebaseStorage,
    ): RealTimeDBRepository =
        RealTimeDBRepositoryImpl(auth, db, storage)
}