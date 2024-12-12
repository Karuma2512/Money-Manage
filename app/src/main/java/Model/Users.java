package Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.ameproject.DatabaseHelper;
import com.example.ameproject.HashUtil;

public class Users {
    public DatabaseHelper dbHelper;
    private String username;
    private String phone;
    private String email;
    private String password;
    private Integer id;
    private String name;

    public Users(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertUser(String username, String password, String phone,
                           String email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", HashUtil.hashPassword(password));
        values.put("phone", phone);
        values.put("email", email);
        values.put("name", name);
        db.insert("users", null, values);
    }
    public void editName(String currentName, String newName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);


        int rowsAffected = db.update("users", values, "name = ?", new String[]{currentName});


        if (rowsAffected > 0) {
            this.name = newName;
        }

        db.close();
    }
    public String getUsername() {
        return username;
    }

    public void setname(String name) {
        this.name = name;
    }
    public String getname() {
        return name;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}