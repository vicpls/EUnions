package netdesigntool.com.eunions.wiki;

import static netdesigntool.com.eunions.Util.LTAG;
import static netdesigntool.com.eunions.Util.getIntegerPart;
import static netdesigntool.com.eunions.wiki.SPARQLquery.AREA_ID;
import static netdesigntool.com.eunions.wiki.SPARQLquery.BASE_URL;
import static netdesigntool.com.eunions.wiki.SPARQLquery.CAP_CUR_AREA;
import static netdesigntool.com.eunions.wiki.SPARQLquery.GDP_PER_CAPITA;
import static netdesigntool.com.eunions.wiki.SPARQLquery.HUM_DEV_IND;
import static netdesigntool.com.eunions.wiki.SPARQLquery.ISO_LANG;
import static netdesigntool.com.eunions.wiki.SPARQLquery.MEMBER;
import static netdesigntool.com.eunions.wiki.SPARQLquery.POPULATION;
import static netdesigntool.com.eunions.wiki.SPARQLquery.POP_ID;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import netdesigntool.com.eunions.BuildConfig;
import netdesigntool.com.eunions.Parameter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class WikiRxDataProvider {

    WikiRxService wikiRxService;


    public LiveData<ArrayList<Parameter>> getMainInfo() {return ldConMainInfo;}

    public LiveData<ArrayList<String>> getMemberships() { return ldMemberships; }

    public LiveData<ArrayList<Parameter>> getLdGDPperCapita() {return ldGDPperCapita;}

    public LiveData<ArrayList<Parameter>> getLdHDI() { return ldHumDevInd; }

    public LiveData<ArrayList<Parameter>> getLdPop() { return ldPopulation; }

    LiveData<ArrayList<Parameter>> ldConMainInfo;    // Осн. информация. Столица и т.п.
    LiveData<ArrayList<String>> ldMemberships;       // Членство в союзах и межд.орг.
    LiveData<ArrayList<Parameter>> ldGDPperCapita;   // ВВП подушевой
    LiveData<ArrayList<Parameter>> ldHumDevInd;     // Индекс чел. развития
    LiveData<ArrayList<Parameter>> ldPopulation;    // Численность населения


    public WikiRxDataProvider() {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        // Include Logger for debug config.
        if (BuildConfig.DEBUG) retrofitBuilder.client(getHttpClient());

        wikiRxService = retrofitBuilder.build().create(WikiRxService.class);
    }

    public void startWdBundleRequests(String isoCountryCode){

        if (BuildConfig.DEBUG) Log.d(LTAG, "WikiRxDataProvider.makeWDRequests("+ isoCountryCode +")");

        requestCountryMainInfo(isoCountryCode);
        requestMemberships(isoCountryCode);
        requestGdpPerCapita(isoCountryCode);
        requestHumanDevIndex(isoCountryCode);
        requestPopulation(isoCountryCode);
    }

    private void requestPopulation(String isoCountryCode) {

        ldPopulation = new MutableLiveData<>();

        startWdRequest(String.format(POPULATION, isoCountryCode, ISO_LANG), resp -> {

            if ( resp.isEmpty()) {
                Log.e(LTAG, POP_ID +" answer is empty");
                return;
            }

            ArrayList<Parameter> result = new ArrayList<>(1);
            Map response = resp.get(0);

            result.add(new Parameter(
                    POP_ID
                    , getRespValue(response, "poptLabel")
                    , ""
                    , getRespValue(response, "lyear")
                    , getIntegerPart(getRespValue(response,"last_pop"))
                    , true
                    ,""));

            ((MutableLiveData) ldPopulation).setValue(result);
        });
    }


    // request country main info and set the answer into ldConMainInfo
    public void requestCountryMainInfo(String isoCountryCode){

        ldConMainInfo = new MutableLiveData<>();

        startWdRequest(String.format(CAP_CUR_AREA, isoCountryCode, ISO_LANG), resp -> {

            if ( resp.isEmpty()){
                Log.e(LTAG, "CAP_CUR_AREA answer is empty");
                return;
            }

            Map response = resp.get(0);
            ArrayList<Parameter> result = new ArrayList<>(3);
            result.add(new Parameter("P36"
                                     , getRespValue(response, "PrCapLabel")
                                     ,""
                                     ,""
                                     ,getRespValue(response, "CapLabel")
                                     , false
                                     ,"" ));

            result.add(new Parameter("P38"
                                     , getRespValue(response, "PrCurLabel")
                                     , ""
                                     , ""
                                     , getRespValue(response, "CurrencyLabel")
                                     , false
                                     , ""));

            result.add(new Parameter( AREA_ID
                                        , getRespValue(response, "PrAreaLabel")
                                        , ""
                                        , ""
                                        , getIntegerPart(getRespValue(response, "Area"))
                                        , true
                                        , getRespValue(response, "ArUnitSymbols")));

            ((MutableLiveData)ldConMainInfo).setValue(result);
        });
    }

    // Request GDP per capita
    public void requestGdpPerCapita(String isoCountryCode){

        ldGDPperCapita = new MutableLiveData<>();

        startWdRequest(String.format(GDP_PER_CAPITA, isoCountryCode, ISO_LANG), resp -> {

            if ( resp.isEmpty()) {
                Log.e(LTAG, "GDP_PER_CAPITA answer is empty");
                return;
            }

            ArrayList<Parameter> result = new ArrayList<>(1);
            Map response = resp.get(0);

            result.add(new Parameter("P2132"
                    , getRespValue(response, "gdplLabel")
                    , ""
                    , getRespValue(response, "lyear")
                    , getIntegerPart(getRespValue(response,"last_gdp"))
                    , true
                    , getRespValue(response, "gdpUnitSymbols")));

            ((MutableLiveData) ldGDPperCapita).setValue(result);

        });
    }

    // Request memberships in union for country
    public void requestMemberships(String isoCountryCode){

        ldMemberships = new MutableLiveData<>();

        startWdRequest(String.format( MEMBER, isoCountryCode, ISO_LANG), response -> {

            ArrayList<String> result = new ArrayList<>(response.size());
            for (Map i: response){
                result.add(getRespValue(i, "quaLabel"));
            }

            ((MutableLiveData<ArrayList<String>>) ldMemberships).setValue(result);
        });
    }


    // Request Index of Human Develop.
    void requestHumanDevIndex(String isoCountryCode){

        ldHumDevInd = new MutableLiveData<>();

        startWdRequest(String.format( HUM_DEV_IND, isoCountryCode, ISO_LANG), resp -> {

            if ( resp.isEmpty()) {
                Log.e(LTAG, "HUM_DEV_IND answer is empty");
                return;
            }

            ArrayList<Parameter> result = new ArrayList<>(1);

            Map response = resp.get(0);
            result.add(new Parameter("P1081"
                    , getRespValue(response, "hdiLabel")
                    ,""
                    , getRespValue(response, "lyear")
                    , getRespValue(response, "last_hdi")
                    , false
                    ,"" ));


            ((MutableLiveData)ldHumDevInd).setValue(result);

        });
    }




    // Common parametrized request.
    void startWdRequest(String sparqlRequest, WikiParser parser){
        Single<WikiResponse> wikiResp = wikiRxService.wikiRxQuery(sparqlRequest);

        wikiResp
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<WikiResponse>() {
                    @Override
                    public void onSuccess(@NonNull WikiResponse wikiResponses) {

                        List<Map> resp;

                        int qty=0;
                        try {
                            resp = wikiResponses.results.bindings;
                            qty = resp.size();
                        }catch (Exception e){
                            Log.e(LTAG, "WikiData response is empty or incorrect. \n"+ e.getMessage());
                            resp = Collections.EMPTY_LIST;
                        }

                        Log.i(LTAG, "WikiData response was received. Qty= "+ qty);

                        if (parser !=null) parser.onReceive(resp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LTAG, "Can't get the info from site. "+ e.getMessage());
                    }
                });
    }



    public interface WikiParser {
        void onReceive (List<Map> response);
    }
    



    // Logger for HTTP requests and responds
    OkHttpClient getHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel( HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }




    /**
     * Extract value for given parameter.
     * @param key name of parameter
     * @return value for given name
     */
    static String getRespValue(Map arMap, String key){
        try {
            //(LinkedHashTreeMap)
            return (((Map)(arMap.get(key))) .get("value")).toString();
        }catch(Exception e){
            Log.e(LTAG, "Cannot parse the value for param="+ key);
            return "";
        }
    }


}
