package netdesigntool.com.eunions.firebase

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import netdesigntool.com.eunions.Util.LTAG


private const val URL_REF ="https://fir-a0980.firebaseio.com/"  // URL of firebase
private const val BASE_NAME = "Country"

class FirebaseDataProvider(cont :Context) {

    private val providerScope = CoroutineScope(Dispatchers.IO)      //Dispatchers.Main)
    private var providerJob = providerScope.launch { fbAppInit(cont) }

    private val baseRef = Firebase.database.getReferenceFromUrl(URL_REF)


    /**
     * Rank of country in the list of World Happiness Index.
     * Map of <year, value> where year as String
     */
    val ldRankWHI : LiveData<Map<String, Number>> = MutableLiveData(HashMap())       // The rank of country in the WHI listing. <year, rank>

    /**
     * Values of World Happiness Index
     */
    val ldWHI : LiveData<Map<String, Float>> = MutableLiveData(HashMap())         // The value of WHI. <year, value>

    /**
     * Launch request to the country base for WHI values.
     */
    fun requestWHI(isoCountryCode :String, title: String) {

        val request = createRequest(isoCountryCode, "whi")

        launchRequest(request, ldWHI as MutableLiveData<Map<String, Float>>, title)
    }


    /**
     * Launch request to the country base for the rank in WHI listing.
     */
    fun requestRankWHI(isoCountryCode :String, title: String) {

        val request =  createRequest(isoCountryCode,"whiRank")

        launchRequest(request, ldRankWHI as MutableLiveData<Map<String, Float>>, title)
    }


    //----------------------------------------------------------------------------------------------------------

    private fun fbAppInit(cont: Context) {
        FirebaseApp.initializeApp(cont)

        try {
            Firebase.database.setPersistenceCacheSizeBytes(1024 * 1024 * 1)     //1Mb for FireBase cache.
            Firebase.database.setPersistenceEnabled(true)
        } catch (dbE: DatabaseException) {
            Log.d(LTAG, dbE.message!!)
        }


        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            SafetyNetAppCheckProviderFactory.getInstance()
        )
    }


    private fun createRequest(isoCountryCode: String, part: String): DatabaseReference{
        return baseRef
            .child(BASE_NAME)
            .child(isoCountryCode.uppercase())
            .child(part)
    }


    // Start the parametrised request to Firebase. Fetch result.
    private fun launchRequest(
        dbRef: DatabaseReference,
        result: MutableLiveData<Map<String, Float>>,
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
        result: MutableLiveData<Map<String, Float>>,
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
                is Long -> result[title] = answer.toFloat()
                is Double -> result[title] = answer.toFloat()

                is Map<*, *> -> result = answer as HashMap<String, Float>
            }
        }

        return result
    }


}