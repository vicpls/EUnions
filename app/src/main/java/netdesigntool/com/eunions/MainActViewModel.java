package netdesigntool.com.eunions;

import static netdesigntool.com.eunions.Util.LTAG;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Deprecated
public class MainActViewModel extends AndroidViewModel {

    private DataRepository dataRepository;

    MutableLiveData<Country[]> ldSchAndEu = new MutableLiveData<>();
    MutableLiveData<Country[]> ldSchen = new MutableLiveData<>();
    MutableLiveData<Country[]> ldEu = new MutableLiveData<>();

    private final AtomicBoolean isFetched = new AtomicBoolean(false);

    public MainActViewModel(@NonNull Application application, DataRepository dataRepository) {
        super(application);
        this.dataRepository = dataRepository;
    }

    private void startFetchLdCountries(){

        Log.d(LTAG, "isFetched= "+ isFetched);

        if ( isFetched.compareAndSet(false, true)){
            new Thread(this::fetchLdCountriesByType).start();
        }
    }

    private void fetchLdCountriesByType(){

        Country[] countries = dataRepository.loadCountries(getApplication().getApplicationContext());

        ArrayList<Country> eu = new ArrayList<>(10);
        ArrayList<Country> schen = new ArrayList<>(10);
        ArrayList<Country> schAndEu = new ArrayList<>(30);

        for(Country c : countries){
            if (c.isEU() & c.isSchen()) schAndEu.add(c); // Shengen + EU
            else
            if (c.isEU()) eu.add(c);     // EU only
            else
            if (c.isSchen()) schen.add(c);            // Shengen only
        }

        ldSchAndEu.postValue(schAndEu.toArray(new Country[0]));
        ldSchen.postValue(schen.toArray(new Country[0]));
        ldEu.postValue(eu.toArray(new Country[0]));

    }



    public MutableLiveData<Country[]> getSchAndEu (){

        startFetchLdCountries(); //fetchLdCountriesByType();

        return ldSchAndEu;
    }

    public MutableLiveData<Country[]> getSchen(){

        startFetchLdCountries(); //fetchLdCountriesByType();

        return ldSchen;
    }

    public MutableLiveData<Country[]> getEu(){

        startFetchLdCountries();  //fetchLdCountriesByType();

        return ldEu;
    }
}
