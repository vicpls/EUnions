package netdesigntool.com.eunions.firebase

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import netdesigntool.com.eunions.Util.LTAG

private const val ref ="https://fir-a0980.firebaseio.com/"  // URL of firebase

class FirebaseDataProvider(cont :Context) {

    private var thread = Thread()

    init {
        thread.run {
            FirebaseApp.initializeApp(cont)
        }
    }

    private val baseRef = Firebase.database.getReferenceFromUrl(ref)
    private val baseName = "Country"

    /**
     * Rank of country in the list of World Happiness Index.
     * Map of <year, value> where year as String
     */
    var ldRankWHI : LiveData<Map<String, Number>> = MutableLiveData(HashMap())       // The rank of country in the WHI listing. <year, rank>

    /**
     * Values of World Happiness Index
     */
    var ldWHI : LiveData<Map<String, Float>> = MutableLiveData(HashMap())         // The value of WHI. <year, value>

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


    private fun createRequest(isoCountryCode: String, part: String): DatabaseReference{
        return baseRef
            .child(baseName)
            .child(isoCountryCode.uppercase())
            .child(part)
    }


    // Start the parametrised request to Firebase. Fetch result.
    private fun launchRequest(
        dbRef: DatabaseReference,
        result: MutableLiveData<Map<String, Float>>,
        title: String
    ) {

        thread.run {

            dbRef.get()
                .addOnSuccessListener {

                    if (it?.value != null) {
                        Log.d(LTAG, "Return=${it.value}")
                        result.value = fbAnswerAdapter(it.value, title)
                    } else {
                        Log.d(LTAG, "No WHI data for isoCountryCode=${dbRef}")
                    }
                }
                .addOnFailureListener { Log.e(LTAG, "Error getting data from firebase", it) }
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