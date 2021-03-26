package netdesigntool.com.eunions.country;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class ModelFactory extends ViewModelProvider.AndroidViewModelFactory{

    private final String iso;
    private final Application app;

    public ModelFactory(String iso, Application app) {
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
        return super.create(modelClass);
    }
}