package com.example.android.topmovies;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RvMainItemDao {

    @Query("SELECT * FROM rvMainItems")
    LiveData<List<RvMainItem>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(RvMainItem... rvMainItem);

    // Deletes a single user
    @Delete
    void deleteMovie(RvMainItem rvMainItem);

    @Query("SELECT * FROM rvMainItems WHERE id= :id")
    RvMainItem loadRvMainItemById(int id);
}
