package netdesigntool.com.eunions.ui.country;

import static netdesigntool.com.eunions.Util.LTAG;
import static netdesigntool.com.eunions.Util.formatValue;
import static netdesigntool.com.eunions.Util.getOneKey;
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

import dagger.hilt.android.AndroidEntryPoint;
import netdesigntool.com.eunions.R;
import netdesigntool.com.eunions.Util;
import netdesigntool.com.eunions.databinding.ActCountryBinding;
import com.hh.data.repo.wiki.Parameter;
import netdesigntool.com.eunions.ui.chart.ChartFragment;
import netdesigntool.com.eunions.ui.chart.ChartVM;

/**
 *      Show information for selected country.
 *      Requires bundle with Strings COUNTRY_ISO, COUNTRY_NAME
 */

@AndroidEntryPoint
public class
CountryAct extends AppCompatActivity {

    public static final String COUNTRY_ISO = "ISO";
    public static final String COUNTRY_NAME = "cNAME";

    private ActCountryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActCountryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getExtras() ==null) {
            Log.w(LTAG, "Act closed. No ISO of country was provided for: "
                    + this.getClass().getSimpleName());
            return;
        }

        String sISO = getIntent().getExtras().getString(COUNTRY_ISO);
        String countryName = getIntent().getExtras().getString(COUNTRY_NAME);

        subscribeWiki(sISO);

        subscribeFireBaseObservers(sISO);

        initViews(sISO, countryName);
    }

    private void subscribeWiki(String sISO) {
        if ( isConnected(this)){
            subscribeWikiObservers(sISO);
        } else {
            userNotify(R.string.no_connection);
        }
    }


    private void userNotify(@StringRes int messageId) {
        Snackbar.make( findViewById(R.id.tvCountryName)
                , messageId
                , Snackbar.LENGTH_LONG)
                .show();
    }


    private void initViews(String sISO, String cName) {

        int cNameIdRes = getResources().getIdentifier(sISO, "string", getPackageName());

        if (cNameIdRes >0)
            binding.tvCountryName.setText(cNameIdRes);
        else
            binding.tvCountryName.setText(cName);

        binding.ivFlag.setImageResource( getResources()
                .getIdentifier("flg_"+ sISO, "drawable", getPackageName()));

        binding.tvLinkToGuide.setText( Util.getTravelGuideUrl(this, sISO, cName));
        binding.tvLinkToGuide.setMovementMethod( LinkMovementMethod.getInstance());
    }

    private void subscribeWikiObservers(String sISO) {

        ViewModelProvider.AndroidViewModelFactory vmFactory =
                new VModelFactory(sISO, getApplication());

        //noinspection ConstantConditions
        CountryActVM viewModel = new ViewModelProvider(this,
                (ViewModelProvider.Factory) vmFactory).get(CountryActVM.class);

        LiveData<ArrayList<Parameter>> ldCountryData = viewModel.getResultStr();
        ldCountryData.observe(this, new myObserver());

        LiveData<ArrayList<String>> ldMembers = viewModel.getMembership();
        ldMembers.observe(this, new MembershipsObserver());
    }

    private void subscribeFireBaseObservers(String sISO) {

        ChartVM fbVModel = new ViewModelProvider(this).get(ChartVM.class);
        fbVModel.requestWHI(sISO, "whi");
        fbVModel.requestRankWHI(sISO, "rank");
        fbVModel.getLdRankWHI().observe(this, new FbParameterObserver());
        fbVModel.getLdWHI().observe(this, new FbChartObserver());
    }


    //  Handler of common parameters for country
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


    //  Handler of requesting a country's membership in organizations.
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
                Log.d(LTAG, this.getClass().getSimpleName()
                        + ": Null or empty answer from Firebase.");
                return;
            }

            // Only first element of fbParam shows
            String year = getOneKey(fbParam);

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
    }



    class FbChartObserver implements Observer<Map<String, Number>>{
        final String tag = "chartWHI";

        @Override
        public void onChanged(Map<String, Number> fbChartData) {

            if (fbChartData ==null || fbChartData.isEmpty()) {
                Log.d(LTAG, this.getClass().getSimpleName()
                        + ": Null or empty answer from Firebase.");
                return;
            }

            // Create place for fragment
            View chartItem = CountryAct.this.getLayoutInflater()
                    .inflate(R.layout.act_country_chart, binding.scrollBox, false);
            binding.scrollBox.addView(chartItem);

            FragmentManager frm = CountryAct.this.getSupportFragmentManager();
            frm.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.frContainerView , ChartFragment.class, null, tag)
                    .commit();

        }
    }


    @SuppressLint("SetTextI18n")
    private View getInfoLineView(Parameter param){

        View result = getLayoutInflater().inflate(R.layout.act_country_item_v2, binding.scrollBox, false);

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


    private View getInfoLineView(String name, Object value){
        View result = getLayoutInflater().inflate(R.layout.act_country_item_v2, binding.scrollBox, false);
        ((TextView) result.findViewById(R.id.tvName)).setText(name);
        ((TextView) result.findViewById(R.id.tvValue)).setText(value.toString());
        return result;
    }


    private void showInfo(Parameter parameter){
        binding.scrollBox.addView(getInfoLineView(parameter));
    }


    private void showInfo(String name, Object value){
        binding.scrollBox.addView(getInfoLineView(name, value));
    }


    private void showInfo(@StringRes int name, Object value){
        showInfo(getResources().getString(name), value);
    }

}
