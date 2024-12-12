package Registr;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ameproject.R;

import Model.Users;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Button registerButton = findViewById(R.id.btncompl);
        EditText username = findViewById(R.id.usernameinput);
        EditText password = findViewById(R.id.inputpassre);
        EditText email = findViewById(R.id.inputemailre);
        EditText phone = findViewById(R.id.inputphonere);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username.getText().toString().equals(""))
                {
                    Toast.makeText(view.getContext(), "Please enter username!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(phone.getText().toString().equals(""))
                {
                    Toast.makeText(view.getContext(), "Please enter phone number!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(email.getText().toString().equals(""))
                {
                    Toast.makeText(view.getContext(), "Please enter email!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(password.getText().toString().equals(""))
                {
                    Toast.makeText(view.getContext(), "Please enter your password!", Toast.LENGTH_LONG).show();
                    return;
                }
                Users users = new Users(view.getContext());
                SQLiteDatabase db = users.dbHelper.getReadableDatabase();
                String query = "SELECT " +
                        "(SELECT COUNT(*) FROM users WHERE username = ?) AS usernameExists, " +
                        "(SELECT COUNT(*) FROM users WHERE email = ?) AS emailExists, " +
                        "(SELECT COUNT(*) FROM users WHERE phone = ?) AS phoneExists";
                Cursor cursor = db.rawQuery(query, new String[]{
                        username.getText().toString(),
                        email.getText().toString(),
                        phone.getText().toString()
                });
                if (cursor.moveToFirst()) {
                    @SuppressLint("Range") int usernameExists =
                            cursor.getInt(cursor.getColumnIndex("usernameExists")) ;
                    @SuppressLint("Range") int emailExists =
                            cursor.getInt(cursor.getColumnIndex("emailExists"));
                    @SuppressLint("Range") int phoneExists =
                            cursor.getInt(cursor.getColumnIndex("phoneExists"));

                    if (usernameExists > 0) {
                        Toast.makeText(view.getContext(), "" +
                                "Username already exists", Toast.LENGTH_LONG).show();
                    } else if (emailExists > 0) {
                        Toast.makeText(view.getContext(),
                                "Email already exists", Toast.LENGTH_LONG).show();
                    } else if (phoneExists > 0) {
                        Toast.makeText(view.getContext(),
                                "Phone number already exists", Toast.LENGTH_LONG).show();
                    } else {
                        users.insertUser(username.getText().toString(),
                                password.getText().toString(),
                                phone.getText().toString(),
                                email.getText().toString()

                        );
                    }
                    Toast.makeText(view.getContext(), "" +
                            "Register Complete", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}