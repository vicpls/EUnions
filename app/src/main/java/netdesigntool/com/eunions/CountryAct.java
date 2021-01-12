package netdesigntool.com.eunions;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import netdesigntool.com.eunions.wiki.HumanReadableNumber;
import netdesigntool.com.eunions.wiki.Parameter;
import netdesigntool.com.eunions.wiki.WikiRxDataProvider;

import static netdesigntool.com.eunions.Util.LTAG;
import static netdesigntool.com.eunions.Util.getIntegerPart;
import static netdesigntool.com.eunions.Util.isConnected;
import static netdesigntool.com.eunions.wiki.SPARQLquery.AREA_ID;
import static netdesigntool.com.eunions.wiki.SPARQLquery.POP_ID;


public class CountryAct extends AppCompatActivity {

    public static final String COUNTRY_ISO = "ISO";

    @BindView(R.id.tvCountryName)
    TextView tvCountryName;

    @BindView(R.id.ivFlag)
    ImageView ivFlag;

    @BindView(R.id.scrollBox)
    LinearLayout scrollBox;

    @BindView(R.id.tvLinkToGuide)
    TextView tvLinkToGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_country);

        if (getIntent().getExtras() ==null) return;

        String sISO = getIntent().getExtras().getString(COUNTRY_ISO);

        if (! isConnected(this)){

            Snackbar.make(findViewById(R.id.tvCountryName), R.string.no_connection, Snackbar.LENGTH_LONG).show();

        }else {

            CountryViewModel viewModel = ViewModelProviders.of(this, new ModelFactory(sISO))
                    .get(CountryViewModel.class);

            PopOnKmObserver popPerKm = new PopOnKmObserver();

            LiveData<ArrayList<Parameter>> liveData = viewModel.getCountryInfo();
            liveData.observe(this, new myObserver());
            liveData.observe(this, popPerKm);

            LiveData<ArrayList<String>> ldMembers = viewModel.getMembership();
            ldMembers.observe(this, new MembershipsObserver());

            LiveData<ArrayList<Parameter>> ldGDP = viewModel.getLdGDP();
            ldGDP.observe(this, new myObserver());

            LiveData<ArrayList<Parameter>> ldPopulation = viewModel.getLdPopulation();
            ldPopulation.observe(this, new myObserver());
            ldPopulation.observe(this, popPerKm);

            LiveData<ArrayList<Parameter>> ldHDI = viewModel.getLdHDI();
            ldHDI.observe(this, new myObserver());
        }

        ButterKnife.bind(this);

        tvCountryName.setText( getResources().getIdentifier(sISO, "string", getPackageName()));
        ivFlag.setImageResource( getResources().getIdentifier("flg_"+ sISO, "drawable", getPackageName()));


        tvLinkToGuide.setText( Util.getTravelGuideUrl(this, sISO));
        tvLinkToGuide.setMovementMethod( LinkMovementMethod.getInstance());

        if (Parameter.getHrProvider()==null){

            Log.d(LTAG, "Create HR Provider");

            Parameter.setHrProvider(new HumanReadableNumber() {
                @Override
                public String roundNumber(String value, int accuracy) {
                    return Util.roundNumber(value, accuracy);
                }

                @Override
                public String getPrefixForNumber(String value) {
                    return Util.getPrefixForNumber(value, getApplicationContext());
                }
            });
        }
    }




    /*  Handler of common parameters for country
     */
    class myObserver implements Observer<ArrayList<Parameter>> {
        @Override
        public void onChanged(@Nullable ArrayList<Parameter> arMap) {

            if (arMap ==null) return;

            ListIterator<Parameter> liParams = arMap.listIterator();

            Parameter param;
            while (liParams.hasNext()){
                param = liParams.next();
                showInfo(param);
            }
        }
    }


    /*  Handler of requesting a country's membership in organizations.
     */
    class MembershipsObserver implements Observer<ArrayList<String>>{
        @Override
        public void onChanged(@Nullable ArrayList<String> members) {

            if (members ==null) return;

            StringBuilder membr = new StringBuilder();
            for (String memb: members){
                membr.append(memb).append(", ");
            }

            showInfo(R.string.memberships, membr.substring(0,membr.length()-2));
        }
    }




    /*  Handler of Density of Population for a country.
        Density calculation from quantity of pop and area.
     */
    class PopOnKmObserver implements Observer<ArrayList<Parameter>> {

        String pop, area, year;

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
            if (param !=null) showInfo(param);
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
                    , getResources().getString(R.string.density)
                    , ""
                    , year
                    , String.valueOf(iPop / iArea)
                    , false
                    , getResources().getString(R.string.density_unit));


        }
    }





    @SuppressLint("SetTextI18n")
    private View getInfoLine(Parameter param){

        View result = getLayoutInflater().inflate(R.layout.act_country_item_v2, null);

        ((TextView) result.findViewById(R.id.tvName))
                .setText(param.pName.substring(0,1).toUpperCase() + param.pName.substring(1)); // Fist letter to Upper case.

        String sQualifiedSting = param.getQualifiedSting();
        TextView tvQualifier= result.findViewById(R.id.tvQualifier);
        if (sQualifiedSting.isEmpty()){
            tvQualifier.setVisibility(View.GONE);
        }else {
            tvQualifier.setText(sQualifiedSting);
        }

        ((TextView) result.findViewById(R.id.tvValue)).setText(param.getHumanReadableValue());

        return result;
    }


    private View getInfoLine(String name, Object value){
        View result = getLayoutInflater().inflate(R.layout.act_country_item_v2, null);
        ((TextView) result.findViewById(R.id.tvName)).setText(name);
        ((TextView) result.findViewById(R.id.tvValue)).setText(value.toString());
        return result;
    }

    private void showInfo(Parameter parameter){
        scrollBox.addView(getInfoLine(parameter));
    }


    private void showInfo(String name, Object value){
        scrollBox.addView(getInfoLine(name, value));
    }


    private void showInfo(@StringRes int name, Object value){
        showInfo(getResources().getString(name), value);
    }



    /* ======================================================================================
                                            ViewModel                                      */

    static class CountryViewModel extends ViewModel {

        private final String iso;
        private final WikiRxDataProvider prov;

        private LiveData<ArrayList<Parameter>> wikiData;
        private LiveData<ArrayList<String>> ldMemberships;
        private LiveData<ArrayList<Parameter>> ldGDP;
        private LiveData<ArrayList<Parameter>> ldHDI;
        private LiveData<ArrayList<Parameter>> ldPopulation;

        public CountryViewModel(String iso) {
            this.iso = iso;
            prov = new WikiRxDataProvider();
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
    }


    static class ModelFactory extends ViewModelProvider.NewInstanceFactory{

        private final String iso;

        public ModelFactory(String iso) {
            super();
            this.iso = iso;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == CountryViewModel.class) {
                return (T) new CountryViewModel(iso);
            }
            return super.create(modelClass);//null;
        }
    }

}
