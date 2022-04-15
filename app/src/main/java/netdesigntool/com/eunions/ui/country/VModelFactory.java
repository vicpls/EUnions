package netdesigntool.com.eunions.ui.country;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

class VModelFactory extends ViewModelProvider.AndroidViewModelFactory{

    private final String iso;
    private final Application app;

    public VModelFactory(String iso, Application app) {
        super(app);
        this.iso = iso;
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create (
            @NonNull Class<T> modelClass,
            @NonNull CreationExtras extras)
    {
        return create(modelClass);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == CountryActVM.class) {
            return (T) new CountryActVM(iso, app);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}