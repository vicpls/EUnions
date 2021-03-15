package netdesigntool.com.eunions.country;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.ListIterator;

import netdesigntool.com.eunions.R;
import netdesigntool.com.eunions.Util;
import netdesigntool.com.eunions.wiki.HumanReadableNumber;
import netdesigntool.com.eunions.wiki.Parameter;
import netdesigntool.com.eunions.wiki.WikiRxDataProvider;

import static netdesigntool.com.eunions.Util.IS_DEB;
import static netdesigntool.com.eunions.Util.LTAG;
import static netdesigntool.com.eunions.Util.getIntegerPart;
import static netdesigntool.com.eunions.wiki.SPARQLquery.AREA_ID;
import static netdesigntool.com.eunions.wiki.SPARQLquery.POP_ID;

public class CountryActViewModel extends AndroidViewModel {

    private final String iso;
    private final WikiRxDataProvider prov;

    private LiveData<ArrayList<Parameter>> wikiData;
    private LiveData<ArrayList<String>> ldMemberships;
    private LiveData<ArrayList<Parameter>> ldGDP;
    private LiveData<ArrayList<Parameter>> ldHDI;
    private LiveData<ArrayList<Parameter>> ldPopulation;

    private MediatorLiveData<ArrayList<Parameter>> resultStr;


    public CountryActViewModel(String iso, Application app) {
        super(app);

        this.iso = iso;
        prov = new WikiRxDataProvider();

        if (Parameter.getHrProvider()==null){

            if (IS_DEB) Log.d(LTAG, "Create HR Provider");

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


    MediatorLiveData<ArrayList<Parameter>> getResultStr(){

        if (resultStr ==null) {
            resultStr = new MediatorLiveData<>();
            addSourceToCountryInfoLD(resultStr);
        }
        return resultStr;
    }

    void addSourceToCountryInfoLD(MediatorLiveData<ArrayList<Parameter>> cInfo){

        cInfo.addSource(getCountryInfo(), PopOnKmObserver.getInstance(getApplication(), cInfo));
        cInfo.addSource(getLdGDP(), PopOnKmObserver.getInstance(getApplication(), cInfo));
        cInfo.addSource(getLdHDI(), PopOnKmObserver.getInstance(getApplication(), cInfo));
        cInfo.addSource(getLdPopulation(), PopOnKmObserver.getInstance(getApplication(), cInfo));
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



    /* Singleton.
    Handler of Density of Population for a country.
        Density calculation from quantity of pop and area.
     */
    private static class PopOnKmObserver implements Observer<ArrayList<Parameter>> {

        static private PopOnKmObserver iam;
        static private Application app;
        static MediatorLiveData<ArrayList<Parameter>> source;

        String pop, area, year;

        private PopOnKmObserver(){}

        synchronized static PopOnKmObserver getInstance(Application application, MediatorLiveData<ArrayList<Parameter>> source){

            app = application;

            if (iam ==null) iam = new PopOnKmObserver();
            PopOnKmObserver.source = source;
            return iam;
        }

        @Override
        public void onChanged(@Nullable ArrayList<Parameter> paramList) {

            if (IS_DEB) Log.d(LTAG, "PopOnKmObserver.onChanged("+ paramList +")");

            if (paramList ==null) return;

            ListIterator<Parameter> liParams = paramList.listIterator();

            Parameter param;
            while (liParams.hasNext()) {

                param = liParams.next();
                if (POP_ID.equals(param.pId)) {
                    pop = param.pValue;
                    year = param.pDate;
                }

                if (AREA_ID.equals(param.pId)) area = param.pValue;
            }

            param = getDensity();
            if (param !=null) paramList.add(param);

            source.setValue(paramList);
        }


        // Calculate density
        private Parameter getDensity(){

            if (pop ==null || pop.isEmpty() || area ==null || area.isEmpty()) return null;

            int iPop, iArea;
            try {
                iPop = Integer.parseInt(pop);
                iArea = Integer.parseInt(getIntegerPart(area));
            } catch (NumberFormatException e) {
                return null;
            }

            return new Parameter(""
                    , app.getResources().getString(R.string.density)
                    , ""
                    , year
                    , String.valueOf(iPop / iArea)
                    , false
                    , app.getResources().getString(R.string.density_unit)
            );
        }
    }


}
