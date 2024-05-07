package com.example.workcareer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "job_listings")
public class JobListing {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String company;
    private String location;
    private boolean isFavorite;

    public JobListing(String title, String company, String location) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.isFavorite = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

}
