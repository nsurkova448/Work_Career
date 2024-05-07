package com.example.workcareer;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JobListingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JobListing jobListing);

    @Delete
    void delete(JobListing jobListing);

    @Query("SELECT * FROM job_listings WHERE isFavorite = 1")
    List<JobListing> getFavoriteJobListings();
}
