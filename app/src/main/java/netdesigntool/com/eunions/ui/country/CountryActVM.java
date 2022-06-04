package netdesigntool.com.eunions.ui.country;

import static com.hh.data.repo.wiki.SPARQLquery.AREA_ID;
import static com.hh.data.repo.wiki.SPARQLquery.POP_ID;
import static netdesigntool.com.eunions.Util.LTAG;
import static netdesigntool.com.eunions.Util.isConnected;

import android.app.Application;
import android.content.res.Resources;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.hh.data.repo.wiki.HumanReadableNumber;
import com.hh.data.repo.wiki.Parameter;
import com.hh.data.repo.wiki.WikiRxDataProvider;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Locale;

import dagger.hilt.EntryPoint;
import dagger.hilt.EntryPoints;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import netdesigntool.com.eunions.R;
import netdesigntool.com.eunions.Util;


public class CountryActVM extends AndroidViewModel {

    private final String iso;
    private final WikiRxDataProvider prov;

    private LiveData<ArrayList<Parameter>> wikiData;
    private LiveData<ArrayList<String>> ldMemberships;
    private LiveData<ArrayList<Parameter>> ldGDP;
    private LiveData<ArrayList<Parameter>> ldHDI;
    private LiveData<ArrayList<Parameter>> ldPopulation;
    private PopOnKmObserver popOnKmObserver;

    private MediatorLiveData<ArrayList<Parameter>> resultStr;

    private MutableLiveData<Spanned> _ldTravelGuide;
    private MutableLiveData<Integer> _ldMessage;

    public CountryActVM(String iso, Application app) {
        super(app);

        this.iso = iso;

        prov = EntryPoints.get(getApplication(), WiKiProviderEntPoint.class).getWiKiProvider();

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

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface WiKiProviderEntPoint{
        WikiRxDataProvider getWiKiProvider();
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

    LiveData<Spanned> getLdTravelGuide(String cName){
        if (_ldTravelGuide ==null){
            _ldTravelGuide = new MutableLiveData<Spanned> (getTravelGuideUrl(cName));
        }
        return _ldTravelGuide;
    }

    LiveData<Integer> getLdMessage(){
         if (_ldMessage == null){
             _ldMessage= new MutableLiveData<Integer>();
             if (! isConnected(getApplication())) _ldMessage.setValue(R.string.no_connection);
         }
        return _ldMessage;
    }

    /* Create url to a travel guide for the country.
     *   Assumed that name of country in particular language included in url. It may be not true.
     */
    Spanned getTravelGuideUrl(String cName){

        Application app = getApplication();

        int idForCountryName =
                app.getResources().getIdentifier(iso, "string",  app.getPackageName());

        String countryName = (idForCountryName >0) ?
                app.getResources().getString(idForCountryName)
                : cName;

        String url = "http://wikitravel.org/"+ Locale.getDefault().getLanguage() +"/"+ countryName;

        String html = " <a href=\""+ url +"\">" + app.getResources().getString(R.string.trv_guide);

        if (Build.VERSION.SDK_INT >= 24)
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        else
            //noinspection deprecation
            return Html.fromHtml(html);
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

            if (pop ==null || area ==null || calculated
                    || pop.isEmpty() || area.isEmpty()) return null;

            int iPop, iArea;
            try {
                iPop = Integer.parseInt(pop);
                //iArea = Integer.parseInt(getIntegerPart(area)); old
                iArea = Integer.parseInt(area);
            } catch (NumberFormatException e) {
                Log.e(LTAG, getClass().getSimpleName()
                        +" getDensity() Connot convert to Int", e);
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
