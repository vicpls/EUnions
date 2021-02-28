package netdesigntool.com.eunions;

import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import netdesigntool.com.eunions.databinding.ActMainBinding;

import static netdesigntool.com.eunions.DataRepository.getDataRepository;
import static netdesigntool.com.eunions.Util.LTAG;

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

        LiveData<Cursor> myData = myVModel.getData();

        myData.observe(this, this::fillUpFlexBox);

    }



    private void fillUpFlexBox(Cursor cursor) {

        int qty = cursor.getCount();

        if (qty > 0) {

            Integer euni =0, schen =0;
            String sISO = null;
            TextView tvCountry = null;
            ImageView ivFlag = null;

            String pacName = getPackageName();

            cursor.moveToFirst();

            for (int i = 0; i < qty; i++) {

                View vFlexBoxItem = createFlexBoxItem (tvCountry, ivFlag);

                getFieldsFromCursor(cursor, sISO, euni, schen);

                vFlexBoxItem.setTag(sISO);

                tvCountry.setText(getResources().getIdentifier(sISO, "string", pacName));
                ivFlag.setImageResource( getResources().getIdentifier("flg_"+ sISO, "drawable", pacName));

                addView2Box(euni, schen, vFlexBoxItem);

                cursor.moveToNext();
            }
        }
    }

    // This method change input parameters!
    private View createFlexBoxItem (TextView tvCountry, ImageView ivFlag){

        View vFlexBoxItem = this.getLayoutInflater().inflate(R.layout.item, null);
        vFlexBoxItem.setOnClickListener(this);

        tvCountry = vFlexBoxItem.findViewById(R.id.tvCountry);
        ivFlag = vFlexBoxItem.findViewById(R.id.ivFlag);

        return vFlexBoxItem;
    }

    // This method change input parameters!
    private void getFieldsFromCursor(Cursor cursor, String sISO, Integer euni, Integer schen){

        sISO = cursor.getString(cursor.getColumnIndex(DataRepository.COL_NAME));
        euni = cursor.getInt(cursor.getColumnIndex(DataRepository.COL_EU));
        schen = cursor.getInt(cursor.getColumnIndex(DataRepository.COL_SHE));
    }



    private void addView2Box(int euni, int schen, View vFlexBoxItem) {

        if (euni <=1 & schen <=1) binding.flexboxMiddle.addView(vFlexBoxItem); // Shengen + EU
        else
        if (euni <=1 & schen >1) binding.flexboxTop.addView(vFlexBoxItem);     // EU only
        else
        if (schen <=1) binding.flexboxBottom.addView(vFlexBoxItem);            // Shengen only
    }


    @Override
    public void onClick(View view) {
        String country = view.getTag().toString();
        Log.d(LTAG,"Click on Country="+ country +";");

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



    //----------------------------------------------------------
    //  ViewModel
    //----------------------------------------------------------
    protected static class MainActViewModel extends AndroidViewModel {

        MutableLiveData<Cursor> cData;

        public MainActViewModel(@NonNull Application application) {
            super(application);
        }


        public LiveData<Cursor> getData(){
            if (cData ==null){
                cData = new MutableLiveData<>();
                cData.setValue(getDataRepository().loadData(getApplication().getApplicationContext()));
            }
            return cData;
        }
    }
}
