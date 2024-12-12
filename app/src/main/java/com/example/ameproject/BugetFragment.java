package com.example.ameproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Apdatper.BudgetAdapter;
import Model.Budget;


public class BugetFragment extends Fragment {

private RecyclerView bugetRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buget, container, false);
        Button addBtn = view.findViewById(R.id.addbtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddBudgetActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        bugetRecyclerView = view.findViewById(R.id.budgetRecyclerView);
        bugetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Budget> budgets = loadBudgets();
        BudgetAdapter adapter = new BudgetAdapter(budgets);
        bugetRecyclerView.setAdapter(adapter);
        return view;
    }
    private List<Budget> loadBudgets()
    {
        List<Budget> budgets = new ArrayList<>();
        SQLiteDatabase db = new DatabaseHelper(getContext()).getReadableDatabase();
        String query = "SELECT b.id, b.amount, b.remaining," +
                "b.category_id, b.user_id,b.start_date, b.end_date, c.name AS category_name " +
                "FROM budget b " +
                "LEFT JOIN categories c ON b.category_id = c.id";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            do
            {
                Integer id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"));
                int remaining = cursor.getInt(cursor.getColumnIndexOrThrow("remaining"));
                Integer categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                Integer userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow("end_date"));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow("category_name"));
Budget budget = new Budget(id,amount,categoryId,userId,startDate,endDate);
budget.setCategoryName(categoryName);
budget.setRemaining(remaining);
budgets.add(budget);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return budgets;
    }
}