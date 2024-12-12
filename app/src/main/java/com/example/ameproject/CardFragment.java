package com.example.ameproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Apdatper.ExpenseAdapter;
import Model.Expense;


public class CardFragment extends Fragment {
    private RecyclerView tranRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        tranRecyclerView = view.findViewById(R.id.tranRecyclerView);
        tranRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Expense> expenses = loadExpense();
        ExpenseAdapter adapter = new ExpenseAdapter(expenses);
        tranRecyclerView.setAdapter(adapter);
        return view;
    }
    private List<Expense> loadExpense()
    {
        List<Expense> expenses = new ArrayList<>();
        SQLiteDatabase db = new DatabaseHelper(getContext()).getReadableDatabase();
        String query = "SELECT e.id, e.amount, e.note, " +
                "e.category_id, e.user_id, e.date, c.name AS category_name " +
                "FROM expense e " +
                "LEFT JOIN categories c ON e.category_id = c.id";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            do
            {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"));
                Integer categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                Integer userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("category_name"));
                String note = cursor.getString(cursor.getColumnIndexOrThrow("note"));
                Expense expense = new Expense(id,amount,categoryId,userId,date,note);
                expense.setCategoryName(categoryName);

                expenses.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return expenses;
    }
}