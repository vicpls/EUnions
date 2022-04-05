package netdesigntool.com.eunions.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import netdesigntool.com.eunions.model.Country
import netdesigntool.com.eunions.local_db.AppDatabase
import netdesigntool.com.eunions.model.toCountry
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class MainActVM @Inject constructor(val appDb : AppDatabase) : ViewModel() {

    val ldSchAndEu : LiveData<List<Country>>
        get() = _ldSchAndEu
    private val _ldSchAndEu = MutableLiveData<List<Country>>()
        get() = field.also{startFetchLdCountries()}

    val ldSchen : LiveData<List<Country>>
        get() = _ldSchen
    private val _ldSchen = MutableLiveData<List<Country>>()
        get() = field.also{startFetchLdCountries()}

    val ldEu  : LiveData<List<Country>>
        get() = _ldEu
    private val _ldEu = MutableLiveData<List<Country>>()
        get() = field.also{startFetchLdCountries()}


    private val isFetched = AtomicBoolean(false)


    private fun startFetchLdCountries(){

        if ( isFetched.compareAndSet(false, true)) {
            CoroutineScope(Dispatchers.IO).launch {
                fetchLdCountries()
            }
        }
    }


    private suspend fun fetchLdCountries(){

        val eu = mutableListOf<Country>()       // EU only
        val schen = mutableListOf<Country>()    // Shengen only
        val schAndEu = mutableListOf<Country>() // Shengen + EU

        appDb.countriesDao()
                .getMemberCountries()
                .map{it.toCountry()}
                .forEach { country ->
                    when {
                        (country.isEU && country.isSchen) -> schAndEu.add(country)
                        (country.isEU) -> eu.add(country)
                        (country.isSchen) -> schen.add(country)
                    }
                }

        _ldEu.postValue(eu)
        _ldSchAndEu.postValue(schAndEu)
        _ldSchen.postValue(schen)
    }

}