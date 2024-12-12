package com.example.ameproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
FrameLayout frameLayout;
BottomNavigationView bottomNavigationView;
HomeFragment homeFragment = new HomeFragment();
ProfileFragment profileFragment = new ProfileFragment();
BugetFragment bugetFragment = new BugetFragment();
CardFragment cardFragment = new CardFragment();
AddFragment addFragment = new AddFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

bottomNavigationView = findViewById(R.id.bottom_navigation);

getSupportFragmentManager().beginTransaction().replace(R.id.main,homeFragment).commit();
bottomNavigationView.setSelectedItemId(R.id.homemain);
bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.homemain) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main,homeFragment).commit();
            return true;
        } else if (itemId == R.id.add) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main,addFragment).commit();
            return true;
        } else if (itemId == R.id.buget) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main, bugetFragment).commit();
            return true;
        } else if (itemId == R.id.transaction) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main, cardFragment).commit();
            return true;
        } else if (itemId == R.id.profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main, profileFragment).commit();
            return true;
        }

        return false;
    }
});
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "amedatabase.db";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_USER_TABLE = "CREATE TABLE users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "phone TEXT, " +
                    "email TEXT, " +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
            db.execSQL(CREATE_USER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        }
    }
}