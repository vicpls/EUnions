package netdesigntool.com.eunions;

import static netdesigntool.com.eunions.DataRepository.getDataRepository;
import static netdesigntool.com.eunions.Util.LTAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.flexbox.FlexboxLayout;

import netdesigntool.com.eunions.country.CountryAct;
import netdesigntool.com.eunions.databinding.ActMainBinding;
import netdesigntool.com.eunions.othcountries.ActOtherCountries;
import netdesigntool.com.eunions.othcountries.FrOtherCountryList;

public class MainActivity extends AppCompatActivity
                            implements View.OnClickListener
{
    private ActMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewModelProvider.Factory vmFactory = (ViewModelProvider.Factory) new VmFactory(getDataRepository());

        MainActViewModel myVModel = new ViewModelProvider(this, vmFactory)
                .get(MainActViewModel.class);

        observeViewModel(myVModel);

        //binding.flOthers.setOnClickListener(new OnOtherCountryClick());
        binding.flOthers.setOnClickListener(new OnOtherCountryClickFr());
    }

    private void observeViewModel(MainActViewModel vm){
        LiveData<Country[]> countryEU = vm.getEu();
        LiveData<Country[]> countrySchEu = vm.getSchAndEu();
        LiveData<Country[]> countrySch = vm.getSchen();

        countryEU.observe(this, this::fillUpFbTop);
        countrySchEu.observe(this, this::fillUpFbMiddle);
        countrySch.observe(this, this::fillUpFbBottom);
    }


    private void fillUpFbTop(Country[] countries) {
        fillUpFlexBox(countries, binding.flexboxTop);
    }

    private void fillUpFbMiddle(Country[] countries) {
        fillUpFlexBox(countries, binding.flexboxMiddle);
    }

    private void fillUpFbBottom(Country[] countries) {
        fillUpFlexBox(countries, binding.flexboxBottom);
    }

    private void fillUpFlexBox(Country[] countries, FlexboxLayout flexbox){
        for (Country country : countries){
            flexbox.addView(createViewForCountry(country));
        }
    }


    private View createViewForCountry(Country country){

        View vFlexBoxItem = this.getLayoutInflater().inflate(R.layout.item, null);
        vFlexBoxItem.setOnClickListener(this);

        vFlexBoxItem.setTag(country.getISO());

        TextView tvCountry = vFlexBoxItem.findViewById(R.id.tvCountry);
        ImageView ivFlag = vFlexBoxItem.findViewById(R.id.ivFlag);

        tvCountry.setText(getResources().getIdentifier(country.getISO(), "string", getPackageName()));
        ivFlag.setImageResource( getResources().getIdentifier("flg_"+ country.getISO(), "drawable", getPackageName()));

        return vFlexBoxItem;
    }

    // Click on country handler
    @Override
    public void onClick(View view) {
        String country = view.getTag().toString();
        Log.d(LTAG,"Click on Country="+ country +";");

        // Start Activity with detail about selected country
        Intent intent = new Intent(this, CountryAct.class);
        intent.putExtra(CountryAct.COUNTRY_ISO, view.getTag().toString());
        startActivity(intent);
    }


    class OnOtherCountryClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getApplicationContext(), ActOtherCountries.class);
            startActivity(intent);
    }
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
    public boolean onCreateOptionsMenu(Menu menu) {

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

    private class VmFactory extends ViewModelProvider.NewInstanceFactory{

        private final DataRepository dataRepository;

        VmFactory(DataRepository dataRepository){
            this.dataRepository = dataRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == MainActViewModel.class) {
                return (T) new MainActViewModel(getApplication(), dataRepository);
            }
            return super.create(modelClass);
        }
    }

}
