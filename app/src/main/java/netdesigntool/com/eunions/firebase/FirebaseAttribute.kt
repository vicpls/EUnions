package netdesigntool.com.eunions.firebase

import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

/**
 *  Attributes of firebase.
 */


class FirebaseAttribute @Inject constructor(){
    val URL_REF = "https://fir-a0980.firebaseio.com/"  // URL of firebase
    val BASE_NAME = "Country"

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface FbAttributeInterface {
        val URL_REF: String
        val BASE_NAME: String
    }
}