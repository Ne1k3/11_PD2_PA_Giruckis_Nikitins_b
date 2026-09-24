package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class FunctionTest {
    private AppDatabase db;
    private User testUser;
    private Product testProduct;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void tearDown() {
        if (testUser != null) {
            db.userDao().delete(testUser);
        }
        if (testProduct != null) {
            db.productDao().delete(testProduct);
        }
        if (db != null) {
            db.close();
        }
    }

    @Test
    public void testDatabase() {
        assertNotNull(db);
        assertNotNull(db.userDao());
        assertNotNull(db.productDao());
    }

    @Test
    public void registrationAndLogin_workCorrectly() {
        String username = "test_user_" + System.currentTimeMillis();

        // Lietotāja reģistrācija
        User newUser = new User("Test", "User", username, "test@example.com", "Password123");
        db.userDao().insert(newUser);
        testUser = db.userDao().findByUsername(username);

        // Pārbaudes
        assertNotNull(testUser);
        assertNotNull(db.userDao().login(username, "Password123"));
        assertNull(db.userDao().login(username, "WrongPassword"));
    }

    @Test
    public void productOperations_workCorrectly() {
        // Produkta pievienošana
        Product newProduct = new Product("Klēpjdators", "content://media/external/images/1");
        db.productDao().insert(newProduct);

        List<Product> products = db.productDao().getAllProducts();
        assertFalse(products.isEmpty());
        assertEquals("Klēpjdators", products.get(0).name);

        // Produkta dzēšana
        db.productDao().delete(products.get(0));
        List<Product> productsAfterDelete = db.productDao().getAllProducts();
        assertTrue(productsAfterDelete.isEmpty());
    }

    @Test
    public void userUpdateAndDelete_workCorrectly() {
        User user = new User("Jānis", "Bērziņš", "janisb", "janis@example.com", "Secret123");
        db.userDao().insert(user);

        User insertedUser = db.userDao().findByUsername("janisb");
        assertNotNull(insertedUser);

        // Profila atjaunināšana (Update)
        insertedUser.email = "janis.new@example.com";
        db.userDao().update(insertedUser);

        User updatedUser = db.userDao().findByUsername("janisb");
        assertEquals("janis.new@example.com", updatedUser.email);

        // Dzēšana (Delete)
        db.userDao().delete(updatedUser);
        assertNull(db.userDao().findByUsername("janisb"));
    }
}
