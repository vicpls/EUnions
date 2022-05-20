package netdesigntool.com.eunions.ui.main

import android.app.Application
import android.content.Intent
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.model.Country
import netdesigntool.com.eunions.model.toCountry
import netdesigntool.com.eunions.repo.local_db.AppDatabase
import netdesigntool.com.eunions.ui.country.CountryAct
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject


sealed class Desc {
    abstract val descr: Int
    abstract val mark: String
    var show : Boolean = false

    object EU : Desc(){
        @StringRes override val descr = R.string.eu_desc
        override val mark
            get() = "EU"
    }
    object Schengen : Desc(){
        @StringRes override val descr = R.string.schengen_desc
        override val mark
            get() = "SC"
    }
}



@HiltViewModel
class MainActVM @Inject constructor(val appDb : AppDatabase, app: Application)
    : AndroidViewModel( app )
{
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

    val ldShowDesc : LiveData<Desc> by this::_ldShowDesc
    private val _ldShowDesc = MutableLiveData<Desc>()


    private val app by lazy(LazyThreadSafetyMode.NONE) {getApplication<Application>()}
    private val myResource by lazy(LazyThreadSafetyMode.NONE) {app.resources}
    private val eu by lazy(LazyThreadSafetyMode.NONE) {myResource.getString(R.string.eu)}
    private val sch by lazy(LazyThreadSafetyMode.NONE) {myResource.getString(R.string.schengen)}

    private var currentDesc : Desc = Desc.EU


    fun onOrganizationClick(desc : Desc)
    {
        if (currentDesc != desc) {

            // If the other fragment visible then remove it.
            if (currentDesc.show){
                currentDesc.show = false
                _ldShowDesc.value = currentDesc
            }

            _ldShowDesc.value =
                desc.apply {
                    show = true
                    currentDesc = desc
                }
        } else
            _ldShowDesc.value = currentDesc.apply { show = !show }
    }


    fun onCountryClick(iso: String, act: AppCompatActivity)
    {
        when (iso) {
            eu -> onOrganizationClick(Desc.EU)
            sch -> onOrganizationClick(Desc.Schengen)
            else -> {
                // Remove fragment if it showing.
                if (currentDesc.show){
                    currentDesc.show = false
                    _ldShowDesc.value = currentDesc
                }

                // Start Activity with detail about selected country
                val intent = Intent(app, CountryAct::class.java)
                    .also { it.putExtra(CountryAct.COUNTRY_ISO, iso) }

                act.startActivity(intent)
            }
        }
    }

    private fun startFetchLdCountries()
    {
        if ( isFetched.compareAndSet(false, true)) {
            CoroutineScope(Dispatchers.IO).launch {
                fetchLdCountries()
            }
        }
    }

    private suspend fun fetchLdCountries()
    {
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