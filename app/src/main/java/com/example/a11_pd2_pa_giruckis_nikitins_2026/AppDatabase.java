package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {User.class, Product.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ProductDao productDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "my_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            seedProducts();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            seedProducts();
        }
    };

    private static void seedProducts() {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (INSTANCE != null) {
                ProductDao dao = INSTANCE.productDao();
                if (dao.getAllProducts().isEmpty()) {
                    dao.insert(new Product("Piens", ""));
                    dao.insert(new Product("Maize", ""));
                    dao.insert(new Product("Siers", ""));
                    dao.insert(new Product("Āboli", ""));
                    dao.insert(new Product("Banāni", ""));
                    dao.insert(new Product("Olas", ""));
                    dao.insert(new Product("Sviests", ""));
                    dao.insert(new Product("Kafija", ""));
                    dao.insert(new Product("Tēja", ""));
                    dao.insert(new Product("Tomāti", ""));
                    dao.insert(new Product("Gurķi", ""));
                    dao.insert(new Product("Sula", ""));
                    dao.insert(new Product("Ūdens", ""));
                    dao.insert(new Product("Cukurs", ""));
                    dao.insert(new Product("Milti", ""));
                }
            }
        });
    }
}
