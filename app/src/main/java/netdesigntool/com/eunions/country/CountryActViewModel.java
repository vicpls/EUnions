package netdesigntool.com.eunions.country;

import static netdesigntool.com.eunions.Util.LTAG;
import static netdesigntool.com.eunions.Util.getIntegerPart;
import static netdesigntool.com.eunions.wiki.SPARQLquery.AREA_ID;
import static netdesigntool.com.eunions.wiki.SPARQLquery.POP_ID;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.ListIterator;

import netdesigntool.com.eunions.Parameter;
import netdesigntool.com.eunions.R;
import netdesigntool.com.eunions.Util;
import netdesigntool.com.eunions.wiki.HumanReadableNumber;
import netdesigntool.com.eunions.wiki.WikiRxDataProvider;


public class CountryActViewModel extends AndroidViewModel {

    private final String iso;
    private final WikiRxDataProvider prov;

    private LiveData<ArrayList<Parameter>> wikiData;
    private LiveData<ArrayList<String>> ldMemberships;
    private LiveData<ArrayList<Parameter>> ldGDP;
    private LiveData<ArrayList<Parameter>> ldHDI;
    private LiveData<ArrayList<Parameter>> ldPopulation;
    private PopOnKmObserver popOnKmObserver;

    private MediatorLiveData<ArrayList<Parameter>> resultStr;


    public CountryActViewModel(String iso, Application app) {
        super(app);

        this.iso = iso;
        prov = new WikiRxDataProvider();

        if (Parameter.getHrProvider()==null){

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

            if (popOnKmObserver ==null) popOnKmObserver= new PopOnKmObserver();

            addSourceToCountryInfoLD(resultStr);
        }

        return resultStr;
    }

    void addSourceToCountryInfoLD(MediatorLiveData<ArrayList<Parameter>> cInfo){

        cInfo.addSource(getCountryInfo(), popOnKmObserver);
        cInfo.addSource(getLdGDP(), popOnKmObserver);
        cInfo.addSource(getLdHDI(), popOnKmObserver);
        cInfo.addSource(getLdPopulation(), popOnKmObserver);
    }

    LiveData<ArrayList<Parameter>> getCountryInfo() {

        if (wikiData == null) {
            prov.startWdBundleRequests(iso);
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


    /*
        Handler of Density of Population for a country.
        Density calculation from quantity of pop and area.
     */
    private class PopOnKmObserver implements Observer<ArrayList<Parameter>> {

        private String pop, area, year;
        private boolean calculated = false;


        private PopOnKmObserver(){}


        @Override
        public void onChanged(@Nullable ArrayList<Parameter> paramList) {

            Log.d(LTAG, "PopOnKmObserver.onChanged("+ paramList +")");

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

            resultStr.setValue(paramList);
        }


        // Calculate density
        private Parameter getDensity(){

            if (pop ==null || area ==null || calculated || pop.isEmpty() || area.isEmpty()) return null;

            int iPop, iArea;
            try {
                iPop = Integer.parseInt(pop);
                iArea = Integer.parseInt(getIntegerPart(area));
            } catch (NumberFormatException e) {
                return null;
            }

            calculated = true;

            Resources res = getApplication().getResources();

            return new Parameter(""
                    , res.getString(R.string.density)
                    , ""
                    , year
                    , String.valueOf(iPop / iArea)
                    , false
                    , res.getString(R.string.density_unit)
            );
        }
    }

}
