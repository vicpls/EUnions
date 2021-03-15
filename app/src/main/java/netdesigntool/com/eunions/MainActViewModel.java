package netdesigntool.com.eunions;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

import static netdesigntool.com.eunions.DataRepository.getDataRepository;

@HiltViewModel
public class MainActViewModel extends AndroidViewModel {

    MutableLiveData<Country[]> ldSchAndEu;
    MutableLiveData<Country[]> ldSchen;
    MutableLiveData<Country[]> ldEu;

    @Inject
    public MainActViewModel(@NonNull Application application) {
        super(application);
    }

    private void fetchLdCountriesByType(){

        Country[] countries = getDataRepository().loadCountries(getApplication().getApplicationContext());   // todo: делать Асинхронно!!

        ldSchAndEu = new MutableLiveData<>();
        ldSchen = new MutableLiveData<>();
        ldEu = new MutableLiveData<>();

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

        ldSchAndEu.setValue(schAndEu.toArray(new Country[0]));
        ldSchen.setValue(schen.toArray(new Country[0]));
        ldEu.setValue(eu.toArray(new Country[0]));
    }



    public MutableLiveData<Country[]> getSchAndEu (){

        if (ldSchAndEu ==null) fetchLdCountriesByType();

        return ldSchAndEu;
    }

    public MutableLiveData<Country[]> getSchen(){

        if (ldSchen ==null) fetchLdCountriesByType();

        return ldSchen;
    }

    public MutableLiveData<Country[]> getEu(){

        if (ldEu ==null) fetchLdCountriesByType();

        return ldEu;
    }
}
