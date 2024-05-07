package com.example.workcareer;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.workcareer.JobListing;
import com.example.workcareer.JobListingDao;

@Database(entities = {JobListing.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract JobListingDao jobListingDao();
}
