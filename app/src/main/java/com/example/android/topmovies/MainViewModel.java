package com.example.android.topmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<RvMainItem>> rvMainItems;

    public MainViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        rvMainItems  = database.rvMainItemDao().getAll();
    }

    public LiveData<ArrayList<RvMainItem>> getRvMainItems() {
        return rvMainItems;
    }
}
