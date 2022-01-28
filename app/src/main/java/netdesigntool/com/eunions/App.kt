package netdesigntool.com.eunions

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import netdesigntool.com.eunions.firebase.FirebaseAttribute
import javax.inject.Inject

@HiltAndroidApp
class App: Application(){
    @Inject
    lateinit var fbAttrib: FirebaseAttribute
}