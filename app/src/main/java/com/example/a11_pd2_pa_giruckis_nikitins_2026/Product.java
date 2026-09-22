package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Product {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String imageUri;

    public Product(String name, String imageUri) {
        this.name = name;
        this.imageUri = imageUri;
    }
}
