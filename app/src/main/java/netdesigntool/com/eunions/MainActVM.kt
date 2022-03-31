package netdesigntool.com.eunions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import netdesigntool.com.eunions.local_db.AppDatabase
import javax.inject.Inject

@HiltViewModel
class MainActVM @Inject constructor(val appDb : AppDatabase, application: Application) : AndroidViewModel(application){

    fun getSchAndEu () : List<Country> {
        launch()
        appDb.countriesDao().getMemberCountries()
    }



}