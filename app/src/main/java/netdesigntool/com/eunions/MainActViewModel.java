package netdesigntool.com.eunions;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import static netdesigntool.com.eunions.DataRepository.getDataRepository;

class MainActViewModel extends AndroidViewModel {

    MutableLiveData<Country[]> ldSchAndEu;
    MutableLiveData<Country[]> ldSchen;
    MutableLiveData<Country[]> ldEu;

    public MainActViewModel(@NonNull Application application) {
        super(application);
    }

    private void fetchLdCountriesByType(){

        Country[] countries = getDataRepository().loadCountries(getApplication().getApplicationContext());

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

        ldSchAndEu.setValue(schAndEu.toArray(new Country[schAndEu.size()]));
        ldSchen.setValue(schen.toArray(new Country[schen.size()]));
        ldEu.setValue(eu.toArray(new Country[eu.size()]));
    }



    public MutableLiveData<Country[]> getSchAndEu (){

        if (ldSchAndEu ==null) fetchLdCountriesByType();

        return ldSchAndEu;
    }

    public MutableLiveData<Country[]> getSchen(){
        if (ldSchen ==null) fetchLdCountriesByType();

        return ldSchAndEu;
    }

    public MutableLiveData<Country[]> getEu(){

        if (ldEu ==null) fetchLdCountriesByType();

        return ldSchAndEu;
    }
}
