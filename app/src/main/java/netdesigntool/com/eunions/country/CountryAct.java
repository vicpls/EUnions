package netdesigntool.com.eunions.country;

import static netdesigntool.com.eunions.Util.LTAG;
import static netdesigntool.com.eunions.Util.isConnected;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;

import netdesigntool.com.eunions.Parameter;
import netdesigntool.com.eunions.R;
import netdesigntool.com.eunions.Util;
import netdesigntool.com.eunions.chart.ChartFragment;
import netdesigntool.com.eunions.chart.FirebaseViewModel;
import netdesigntool.com.eunions.databinding.ActCountryBinding;


public class CountryAct extends AppCompatActivity {

    public static final String COUNTRY_ISO = "ISO";

    private ActCountryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActCountryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getExtras() ==null) {
            Log.w(LTAG, "Act closed. No ISO of country was provided for: " + this.getClass().getSimpleName());
            return;
        }

        String sISO = getIntent().getExtras().getString(COUNTRY_ISO);

        if (! isConnected(this)){
            Snackbar.make( findViewById(R.id.tvCountryName)
                    , R.string.no_connection
                    , Snackbar.LENGTH_LONG)
                    .show();
        }else {
            //subscribeFireBaseObservers(sISO);
            subscribeWikiObservers(sISO);
        }
        subscribeFireBaseObservers(sISO);

        initViews(sISO);
    }
    

    private void initViews(String sISO) {
        binding.tvCountryName.setText( getResources().getIdentifier(sISO, "string", getPackageName()));
        binding.ivFlag.setImageResource( getResources().getIdentifier("flg_"+ sISO, "drawable", getPackageName()));

        binding.tvLinkToGuide.setText( Util.getTravelGuideUrl(this, sISO));
        binding.tvLinkToGuide.setMovementMethod( LinkMovementMethod.getInstance());
    }

    private void subscribeWikiObservers(String sISO) {

        ViewModelProvider.AndroidViewModelFactory vmFactory = new ModelFactory(sISO, getApplication());

        CountryActViewModel viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) vmFactory)
                .get(CountryActViewModel.class);


        LiveData<ArrayList<Parameter>> liveData = viewModel.getResultStr();
        liveData.observe(this, new myObserver());

        LiveData<ArrayList<String>> ldMembers = viewModel.getMembership();
        ldMembers.observe(this, new MembershipsObserver());
    }

    private void subscribeFireBaseObservers(String sISO) {
        FirebaseViewModel fbVModel = new ViewModelProvider(this).get(FirebaseViewModel.class);
        fbVModel.requestWHI(sISO, "whi");
        fbVModel.requestRankWHI(sISO, "rank");
        fbVModel.getLdRankWHI().observe(this, new FbParameterObserver());
        fbVModel.getLdWHI().observe(this, new FbChartObserver());
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

            if (membr.length()-2 >0) {
                showInfo(R.string.memberships, membr.substring(0, membr.length() - 2));
            }
        }
    }


    class FbParameterObserver implements Observer<Map<String, Number>>{
        @Override
        public void onChanged(Map<String, Number> fbParam) {

            if (fbParam ==null || fbParam.isEmpty()) {
                Log.d(LTAG, this.getClass().getSimpleName() + ": Null or empty answer from Firebase.");
                return;
            }

            // Only first element of fbParam shows
            String year = getOne(fbParam);

            showInfo(
                    new Parameter(""
                            , getResources().getString(R.string.title_rank_whi)
                            , ""
                            , year
                            , formatValue( fbParam.get(year))
                            , false
                            , ""
                    )
            );
        }


        // TODO: return the last value!
        private <T> String getOne(Map<String, T> mMap){

            String result="";

            for (String k : mMap.keySet()) {
                result = k;
            }
            return result;
        }


        String formatValue(Number num) {
            String result;

            if (num instanceof Float) {
                Float f = num.floatValue();

                if (f - f.intValue() < 0.001) {
                    result = Util.getIntegerPart(f.toString());
                } else {
                    result = f.toString();
                }

            } else {
                result = num.toString();
            }

            return result;
        }
    }



    class FbChartObserver implements Observer<Map<String, Float>>{
        final String tag = "chartWHI";

        @Override
        public void onChanged(Map<String, Float> fbChartData) {

            if (fbChartData ==null || fbChartData.isEmpty()) {
                Log.d(LTAG, this.getClass().getSimpleName() + ": Null or empty answer from Firebase.");
                return;
            }

            // Create place for fragment
            View chartItem = CountryAct.this.getLayoutInflater().inflate(R.layout.act_country_chart, null, false);
            binding.scrollBox.addView(chartItem);

            FragmentManager frm = CountryAct.this.getSupportFragmentManager();
            frm.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.frContainerView , ChartFragment.class, null, tag)
                    .commit();

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
        binding.scrollBox.addView(getInfoLine(parameter));
    }


    private void showInfo(String name, Object value){
        binding.scrollBox.addView(getInfoLine(name, value));
    }


    private void showInfo(@StringRes int name, Object value){
        showInfo(getResources().getString(name), value);
    }

}
