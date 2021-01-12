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
import androidx.lifecycle.ViewModelProviders;

import com.google.android.flexbox.FlexboxLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import static netdesigntool.com.eunions.DataRepository.getDataRepository;
import static netdesigntool.com.eunions.Util.LTAG;

public class MainActivity extends AppCompatActivity
                            implements View.OnClickListener
{

    @BindView(R.id.flexboxTop)
    FlexboxLayout fbTop;

    @BindView(R.id.flexboxMiddle)
    FlexboxLayout fbMiddle;

    @BindView(R.id.flexboxBottom)
    FlexboxLayout fbBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        MainActViewModel myVModel = ViewModelProviders.of(this).get(MainActViewModel.class);
        LiveData<Cursor> myData = myVModel.getData();

        ButterKnife.bind(this);

        myData.observe(this, this::fillUpFlexBox);

    }



    private void fillUpFlexBox(Cursor cursor) {

        int qty = cursor.getCount();

        if (qty > 0) {

            int euni, schen;
            String sISO;

            String pacName = getPackageName();

            cursor.moveToFirst();

            for (int i = 0; i < qty; i++) {

                View vFlexBoxItem = this.getLayoutInflater().inflate(R.layout.item, null);
                vFlexBoxItem.setOnClickListener(this);

                TextView tvCountry = vFlexBoxItem.findViewById(R.id.tvCountry);
                ImageView ivFlag = vFlexBoxItem.findViewById(R.id.ivFlag);

                sISO = cursor.getString(cursor.getColumnIndex(DataRepository.COL_NAME));
                vFlexBoxItem.setTag(sISO);

                tvCountry.setText(getResources().getIdentifier(sISO, "string", pacName));
                ivFlag.setImageResource( getResources().getIdentifier("flg_"+ sISO, "drawable", pacName));

                euni = cursor.getInt(cursor.getColumnIndex(DataRepository.COL_EU));
                schen = cursor.getInt(cursor.getColumnIndex(DataRepository.COL_SHE));

                if (euni <=1 & schen <=1) fbMiddle.addView(vFlexBoxItem);
                else
                if (euni <=1 & schen >1) fbTop.addView(vFlexBoxItem);
                else
                if (schen <=1) fbBottom.addView(vFlexBoxItem);

                cursor.moveToNext();
            }
        }
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
