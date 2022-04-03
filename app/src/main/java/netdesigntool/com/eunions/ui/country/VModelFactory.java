package netdesigntool.com.eunions.ui.country;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

class VModelFactory extends ViewModelProvider.AndroidViewModelFactory{

    private final String iso;

    @io.reactivex.annotations.NonNull
    @Override
    public <T extends ViewModel> T create(@io.reactivex.annotations.NonNull Class<T> modelClass, @io.reactivex.annotations.NonNull CreationExtras extras) {
        return create(modelClass);
    }

    private final Application app;

    public VModelFactory(String iso, Application app) {
        super(app);
        this.iso = iso;
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == CountryActViewModel.class) {
            return (T) new CountryActViewModel(iso, app);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
        //return super.create(modelClass);
    }
}