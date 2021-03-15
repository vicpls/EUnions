package netdesigntool.com.eunions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.flexbox.FlexboxLayout;

import netdesigntool.com.eunions.country.CountryAct;
import netdesigntool.com.eunions.databinding.ActMainBinding;

import static netdesigntool.com.eunions.Util.IS_DEB;
import static netdesigntool.com.eunions.Util.LTAG;

//@AndroidEntryPoint
public class MainActivity extends AppCompatActivity
                            implements View.OnClickListener
{
    private ActMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainActViewModel myVModel = new ViewModelProvider(this).get(MainActViewModel.class);

        attachViewModel(myVModel);
    }

    private void attachViewModel(MainActViewModel vm){
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


    @Override
    public void onClick(View view) {
        String country = view.getTag().toString();
        if (IS_DEB) Log.d(LTAG,"Click on Country="+ country +";");

        // Start Activity with detail about selected country
        Intent intent = new Intent(this, CountryAct.class);
        intent.putExtra(CountryAct.COUNTRY_ISO, view.getTag().toString());
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

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
