package netdesigntool.com.eunions.firebase

import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

/**
 *  Attributes of firebase.
 */

@Singleton
class FirebaseAttribute @Inject constructor() {

    val URL_REF = "https://fir-a0980.firebaseio.com/"  // URL of firebase
    val BASE_NAME = "Country"

}