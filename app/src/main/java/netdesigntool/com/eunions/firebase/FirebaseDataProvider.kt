package netdesigntool.com.eunions.firebase

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FirebaseDataProvider(cont :Context) {

    val fbApp : FirebaseApp? = FirebaseApp.initializeApp(cont)

    var ldRankWHI : LiveData<Map<String, Float>>? = null      // место в списке WHI <год, значение>
    var ldWHI : LiveData<Map<String, Float>>? = null         // значение индекса WHI <год, значение>


    private fun getFBData(country :String, ) {

        val database = Firebase.database
        val myRef = database.getReferenceFromUrl("https://fir-a0980.firebaseio.com/")


        myRef.child("Country")
            .child(country)
            .child("whi")
            .get()
            .addOnSuccessListener {
                Log.d("firebase", "Return=${it?.value}")

                if (it?.value !=null) {
                    if (ldWHI ==null) ldWHI = MutableLiveData()
                    (ldWHI as MutableLiveData <Map<String, Float>>).value = (it.value as Map<String, Float>)
                }else{
                    TODO()// нет данных
                }

            }
            .addOnFailureListener { Log.e("firebase", "Error getting data", it) }
    }
}