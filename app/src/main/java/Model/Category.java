package Model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.ameproject.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private DatabaseHelper dbHelper;
    private Integer id;
    private String name;
    private Integer parentId;
    private Integer userId;

    public Category(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public Category(Integer id, String name, Integer parentId, Integer userId) {
        this.setId(id);
        this.setName(name);
        this.setParentId(parentId);
        this.setUserId(userId);
    }

    @SuppressLint("Range")
    public List<Category> getAllCategory() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Categories",
                new String[]{"id", "name", "parent_Id", "user_Id"},
                null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                Integer parentId = cursor.isNull(cursor.getColumnIndex("parent_id"))
                        ? null : cursor.getInt(cursor.getColumnIndex("parent_id"));

                Integer userId = cursor.isNull(cursor.getColumnIndex("user_id"))
                        ? null : cursor.getInt(cursor.getColumnIndex("user_id"));
                categories.add(new Category(id, name, parentId, userId));
            }
            cursor.close();
        }
        return categories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getName();
    }
    public void insertCategory(String name, Integer ParentId, Integer userId)
    {
     SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("parent_id", parentId);
        values.put("user_id", userId);
       db.insert("categories", null, values);

    }
}


