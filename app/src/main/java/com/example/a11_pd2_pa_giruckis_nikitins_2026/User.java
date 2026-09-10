package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public User(String name) {
        this.name = name;
    }
}
