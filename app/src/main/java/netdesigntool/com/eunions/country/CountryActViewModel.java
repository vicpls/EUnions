package netdesigntool.com.eunions.country;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import netdesigntool.com.eunions.Util;
import netdesigntool.com.eunions.wiki.HumanReadableNumber;
import netdesigntool.com.eunions.wiki.Parameter;
import netdesigntool.com.eunions.wiki.WikiRxDataProvider;

import static netdesigntool.com.eunions.Util.LTAG;

public class CountryActViewModel extends AndroidViewModel {

    private final String iso;
    private final WikiRxDataProvider prov;

    private LiveData<ArrayList<Parameter>> wikiData;
    private LiveData<ArrayList<String>> ldMemberships;
    private LiveData<ArrayList<Parameter>> ldGDP;
    private LiveData<ArrayList<Parameter>> ldHDI;
    private LiveData<ArrayList<Parameter>> ldPopulation;


    public CountryActViewModel(String iso, Application app) {
        super(app);

        this.iso = iso;
        prov = new WikiRxDataProvider();

        if (Parameter.getHrProvider()==null){

            Log.d(LTAG, "Create HR Provider");

            Parameter.setHrProvider(new HumanReadableNumber() {
                @Override
                public String roundNumber(String value, int accuracy) {
                    return Util.roundNumber(value, accuracy);
                }

                @Override
                public String getPrefixForNumber(String value) {
                    return Util.getPrefixForNumber(value, getApplication());
                }
            });
        }
    }

    LiveData<ArrayList<Parameter>> getCountryInfo() {

        if (wikiData == null) {
            prov.makeWDRequests(iso);
            wikiData = prov.getMainInfo();
        }

        return wikiData;
    }

    LiveData<ArrayList<String>> getMembership(){

        if (ldMemberships == null) {
                    ldMemberships = prov.getMemberships();
        }
        return ldMemberships;
    }

    LiveData<ArrayList<Parameter>>  getLdGDP(){
        if (ldGDP ==null) ldGDP = prov.getLdGDPperCapita();
        return ldGDP;
    }

    LiveData<ArrayList<Parameter>>  getLdHDI(){
        if (ldHDI ==null) ldHDI = prov.getLdHDI();
        return ldHDI;
    }

    LiveData<ArrayList<Parameter>> getLdPopulation(){
        if (ldPopulation ==null) ldPopulation = prov.getLdPop();
        return ldPopulation;
    }




    static class ModelFactory extends ViewModelProvider.AndroidViewModelFactory{

        private final String iso;
        private final Application app;

        public ModelFactory(String iso, Application app) {
            super(app);
            this.iso = iso;
            this.app = app;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == CountryActViewModel.class) {
                return (T) new CountryActViewModel(iso, app);
            }
            return super.create(modelClass);
        }
    }
}
