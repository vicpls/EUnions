package netdesigntool.com.eunions.repo.firebase

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import netdesigntool.com.eunions.Util.LTAG
import javax.inject.Inject
import javax.inject.Singleton

/**
 *  Initialise and provide access to Firebase.
 */

@Singleton
class FirebaseDataProvider @Inject constructor(
    @ApplicationContext app: Context,
    private val fbAttrib: FirebaseAttribute
) {

    private val providerScope = CoroutineScope(Dispatchers.IO)
    private var providerJob = providerScope.launch { fbAppInit(app) }

    private val baseRef = Firebase.database.getReferenceFromUrl(fbAttrib.URL_REF)


    /**
     * Launch request to the countries' base for WHI values.
     */
    fun requestWHI(isoCountryCode :String,
                   title: String,
                   result: MutableLiveData<Map<String, Number>>) {

        val request = createRequest(isoCountryCode, "whi")
        launchRequest(request, result, title)
    }


    /**
     * Launch request to the country base for the rank in WHI listing.
     */
    fun requestRankWHI(isoCountryCode :String,
                       title: String,
                       result: MutableLiveData<Map<String, Number>>) {

        val request =  createRequest(isoCountryCode,"whiRank")
        launchRequest(request, result, title)
    }


    private fun fbAppInit(cont: Context) {
        FirebaseApp.initializeApp(cont)

        try {
            with(Firebase.database){
                setPersistenceCacheSizeBytes(1024 * 1024 * 1)     // 1Mb for FireBase cache.
                setPersistenceEnabled(true)                       // Cashing enable.
            }
        } catch (dbE: DatabaseException) {
            Log.d(LTAG, dbE.message ?: "Firebase exeption")
        }


        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }


    private fun createRequest(isoCountryCode: String, part: String): DatabaseReference =
        baseRef
            .child(fbAttrib.BASE_NAME)
            .child(isoCountryCode.uppercase())
            .child(part)


    // Start the parametrised request to Firebase. Fetch result.
    private fun launchRequest(
        dbRef: DatabaseReference,
        result: MutableLiveData<Map<String, Number>>,
        title: String
    ) {
        val previousJob = providerJob

        providerJob = providerScope.launch {

            previousJob.join()          // Previous job must be done.

            dbRef.get()
                .addOnSuccessListener {
                    responseProcessing(it, result, title, dbRef)
                }
                .addOnFailureListener { Log.e(LTAG, "Error getting data from firebase.", it) }
        }
    }


    private fun responseProcessing(
        ds: DataSnapshot,
        result: MutableLiveData<Map<String, Number>>,
        title: String,
        dbRef: DatabaseReference
    ) {
        if (ds.value != null) {
            Log.d(LTAG, "Return=${ds.value}")
            result.value = fbAnswerAdapter(ds.value, title)
        } else {
            Log.d(LTAG, "No WHI data for request=${dbRef}")
        }
    }


    // Process of the subset, valued for this app only of the all possible answers
    private fun fbAnswerAdapter(answer: Any?, title: String) : Map<String, Float> {

        var result = HashMap<String, Float>()

        if (answer !=null) {

            when (answer) {
                is Long   -> result[title] = answer.toFloat()
                is Double -> result[title] = answer.toFloat()
                is HashMap<*,*> -> result = answer as HashMap<String, Float>
            }
        }

        return result
    }


}