package netdesigntool.com.eunions.ui.main;

import static netdesigntool.com.eunions.Util.LTAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
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


    //=============================================================================================

    private void showDesc(MainActVM.Desc desc) {

        Fragment fr = getFragment(desc);

        if ( fr !=null && fr.isVisible()) {
            removeFragmentTrans(fr);
            return;
        } else addFragmentTrans(getPlaceId(desc), fr);

        Fragment anotherFr = getAnotherFr(desc);
        if ( anotherFr !=null && anotherFr.isVisible()) removeFragmentTrans(anotherFr);
    }

    @Nullable
    private Fragment getAnotherFr(MainActVM.Desc desc){

        Pair<Fragment, Fragment> frags = findDescFragments();
        Fragment result = null;

        if (desc.getClass() == MainActVM.Desc.EU.class) result = frags.second;
        if (desc.getClass() == MainActVM.Desc.Schengen.class) result = frags.first;

        return result;
    }

    private int getPlaceId(MainActVM.Desc desc){
        int result = 0;
        if (desc.getClass() == MainActVM.Desc.EU.class) result = R.id.lfDescPlace;
        if (desc.getClass() == MainActVM.Desc.Schengen.class) result = R.id.rtDescPlace;
        return result;
    }

    // Return an appropriate fragment for showing description text. Create it if it not exist.
    @Nullable
    private Fragment getFragment(MainActVM.Desc desc){

        Pair<Fragment, Fragment> frags = findDescFragments();
        Fragment result = null;

        // For EU - "left" fragment
        if (desc.getClass() == MainActVM.Desc.EU.class)
            result = (frags.first !=null) ? frags.first
                    : DescFrag.newInstance(desc.getDescr()
                    , getResources().getColor(R.color.euroUnionNoA)
                    , Color.WHITE);

        // For Schengen - "right" fragment
        if (desc.getClass() == MainActVM.Desc.Schengen.class)
            result = (frags.second !=null) ? frags.second
                    : DescFrag.newInstance(desc.getDescr()
                    , getResources().getColor(R.color.schengenNoA)
                    , Color.BLACK);

        return result;
    }

    private void addFragmentTrans(@IdRes int placeId, Fragment fr){
        getSupportFragmentManager()
                .beginTransaction()
                .add(placeId, fr)
                .addToBackStack(null)
                //.setCustomAnimations(R.anim.to_left_in, R.anim.to_left_out, R.anim.to_right_in, R.anim.to_right_out)
                .commit();
    }

    private void removeFragmentTrans(Fragment fr){
        getSupportFragmentManager()
                .beginTransaction()
                .remove(fr)
                //.setCustomAnimations(R.anim.to_left_in, R.anim.to_left_out, R.anim.to_right_in, R.anim.to_right_out)
                .commit();
    }

    // Find left and right existing fragments for showing description text.
    private Pair<Fragment, Fragment> findDescFragments(){

        Fragment left, right;
        FragmentManager fm = getSupportFragmentManager();

        int placeId = binding.lfDescPlace.getId();
        left = fm.findFragmentById(placeId);

        placeId = binding.rtDescPlace.getId();
        right = fm.findFragmentById(placeId);

        return new Pair<>(left,right);
    }
    // =============================================================================================


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

        mainActVM.onCountryClick(country, this);
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
