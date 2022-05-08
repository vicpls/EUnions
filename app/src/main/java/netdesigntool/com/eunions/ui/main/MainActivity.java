package netdesigntool.com.eunions.ui.main;

import static netdesigntool.com.eunions.Util.LTAG;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import netdesigntool.com.eunions.R;
import netdesigntool.com.eunions.databinding.ActMainBinding;
import netdesigntool.com.eunions.model.Country;
import netdesigntool.com.eunions.ui.AboutAct;
import netdesigntool.com.eunions.ui.description.DescFrag;
import netdesigntool.com.eunions.ui.othcountries.FrOtherCountryList;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity
                            implements View.OnClickListener
{
    private ActMainBinding binding;

    private MainActVM mainActVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainActVM = new ViewModelProvider(this).get(MainActVM.class);

        observeViewModel(mainActVM);

        binding.flOthers.setOnClickListener(new OnOtherCountryClickFr());
    }

    private void observeViewModel(MainActVM vm){
        LiveData<List<Country>> countryEU = vm.getLdEu();
        LiveData<List<Country>> countrySchEu = vm.getLdSchAndEu();
        LiveData<List<Country>> countrySch = vm.getLdSchen();
        LiveData<MainActVM.Desc> showDesc = vm.getLdShowDesc();

        countryEU.observe(this, this::fillUpFbTop);
        countrySchEu.observe(this, this::fillUpFbMiddle);
        countrySch.observe(this, this::fillUpFbBottom);
        showDesc.observe(this, this::showDesc);

    }

    private void showDesc(MainActVM.Desc desc) {

        FragmentManager fm = getSupportFragmentManager();

        int placeId = binding.lfDescPlace.getId();
        Fragment descFrag = fm.findFragmentById(placeId);

        if (descFrag == null) descFrag = DescFrag.newInstance(desc.getDesc());

        if ( ! descFrag.isVisible() ) {
            fm.beginTransaction()
                .add(placeId, descFrag)
                //.setCustomAnimations(R.anim.to_right_out, R.anim.to_right_in)
                .addToBackStack(null)
                .commit();
        } else {
            fm.beginTransaction()
                    .remove(descFrag)
                    //.setCustomAnimations(R.anim.to_right_out, R.anim.to_right_in)
                    .commit();
        }
    }


    private void fillUpFbTop(List<Country> countries) {
        fillUpFlexBox(countries, binding.flexboxTop);
    }

    private void fillUpFbMiddle(List<Country> countries) {
        fillUpFlexBox(countries, binding.flexboxMiddle);
    }

    private void fillUpFbBottom(List<Country> countries) {
        fillUpFlexBox(countries, binding.flexboxBottom);
    }

    private void fillUpFlexBox(List<Country> countries, FlexboxLayout flexbox){
        for (Country country : countries){
            flexbox.addView(createViewForCountry(country));
        }
    }


    private View createViewForCountry(Country country){

        View vFlexBoxItem = this.getLayoutInflater().inflate(R.layout.item, null);
        vFlexBoxItem.setOnClickListener(this);

        vFlexBoxItem.setTag(country.getIso());

        TextView tvCountry = vFlexBoxItem.findViewById(R.id.tvCountry);
        ImageView ivFlag = vFlexBoxItem.findViewById(R.id.ivFlag);

        tvCountry.setText(getResources().getIdentifier(country.getIso(), "string", getPackageName()));
        ivFlag.setImageResource( getResources().getIdentifier("flg_"+ country.getIso(), "drawable", getPackageName()));

        return vFlexBoxItem;
    }

    // Click on country handler
    @Override
    public void onClick(View view) {
        String country = view.getTag().toString();
        Log.d(LTAG,"Click on Country="+ country +";");

        mainActVM.onCountryClick(country);
    }

    class OnOtherCountryClickFr implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        FragmentManager fm = MainActivity.this.getSupportFragmentManager();
            fm.beginTransaction()
                .add(binding.flRoot.getId(), FrOtherCountryList.class, null)
                .addToBackStack(null)
                .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() ==R.id.about){
            new AboutAct().startAboutAct(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
