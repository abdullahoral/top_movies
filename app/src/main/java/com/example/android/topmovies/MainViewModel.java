package com.example.android.topmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<RvMainItem>> rvMainItems;
    public MenuItem optionsItem;

    public MainViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        rvMainItems = database.rvMainItemDao().getAll();
    }

    public LiveData<List<RvMainItem>> getRvMainItems() {
        return rvMainItems;
    }

}
