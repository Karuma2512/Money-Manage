package com.example.ameproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Model.Users;
import Registr.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
    EditText usernameinput, passwordinput;
    Button btnlogin;
    TextView amelink, papaya;
    Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        auth = new Auth(getBaseContext());
        if (auth.getUserId() > 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Kết thúc Activity hiện tại nếu đã đăng nhập
        }

        amelink = findViewById(R.id.amelink);
        String linkame = "<a href='https://www.youtube.com/@WatsonAmelia'>Forgot Password ?</a>";
        amelink.setMovementMethod(LinkMovementMethod.getInstance());
        amelink.setText(Html.fromHtml(linkame));

        papaya = findViewById(R.id.papaya);
        papaya.setPaintFlags(papaya.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        papaya.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        usernameinput = findViewById(R.id.usernameinput);
        passwordinput = findViewById(R.id.passwordinput);
        Users users = new Users(getBaseContext());
        SQLiteDatabase db = users.dbHelper.getReadableDatabase();
        btnlogin = findViewById(R.id.btnlogin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnlogin.setOnClickListener(view -> {
            String userNameinput = usernameinput.getText().toString();
            String passWordinput = passwordinput.getText().toString();

            Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?",
                    new String[]{userNameinput, HashUtil.hashPassword(passWordinput)});
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));

                auth.saveUser(id, name, phone, email, username);

                if (name == null || name.isEmpty()) {
                    showNameInputDialog(id, db);
                } else {
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    view.getContext().startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(view.getContext(), "Invalid Username or Password", Toast.LENGTH_LONG).show();
            }
            cursor.close();
        });

        overridePendingTransition(0, 0);
    }


    private void showNameInputDialog(int userId, SQLiteDatabase db) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View customView = inflater.inflate(R.layout.cus_dialog, null);


        EditText input = customView.findViewById(R.id.name_input);
        Button saveButton = customView.findViewById(R.id.btn_save);



        builder.setView(customView);

        // Save button listener
        saveButton.setOnClickListener(v -> {
            String name = input.getText().toString().trim();
            if (!name.isEmpty()) {
                saveUserName(userId, name, db);
                auth.setName(name);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        AlertDialog dialog = builder.create();
        dialog.show();


        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
    }


    private void saveUserName(int userId, String userName, SQLiteDatabase db) {
        String updateQuery = "UPDATE users SET name = ? WHERE id = ?";
        db.execSQL(updateQuery, new Object[]{userName, userId});
    }
}
