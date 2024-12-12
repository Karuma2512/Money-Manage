package com.example.ameproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.List;

import Apdatper.CategoryAdapter;
import Model.Budget;
import Model.Category;

public class AddBudgetActivity extends AppCompatActivity {
    EditText Amount;
    EditText txtstart;
    EditText txtend;
    TextView selectCategory;
    Button saveBudget;
    Category selectedCategory;
    Auth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_budget);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        selectCategory = findViewById(R.id.selectcategory);
        selectCategory.setOnClickListener(view -> showCategoryDialog());
        auth = new Auth(this.getBaseContext());
        saveBudget = findViewById(R.id.saveBudget);
        saveBudget.setOnClickListener(view -> {
            Amount = findViewById(R.id.txtAmount);
            String startDateString = txtstart.getText().toString();
            String endDateString = txtend.getText().toString();
            Budget budget = new Budget(this);
            budget.insertBudget(
                    Integer.valueOf(Amount.getText().toString()),selectedCategory.getId(),
                    auth.getUserId(),startDateString, endDateString);
            Toast.makeText(getBaseContext(), "Created budget success!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this.getBaseContext(), MainActivity.class);
            this.getBaseContext().startActivity(intent);
        });




        txtstart = findViewById(R.id.startdate);
        txtend = findViewById(R.id.enddate);
        auth = new Auth(getBaseContext());
        txtstart.setOnTouchListener((v, event) ->
        {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showStartDatePickerDialog();
            }
            return true;
        });
        txtend.setOnTouchListener((v, event) ->
        {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showEndDatePickerDialog();
            }
            return true;
        });
        selectCategory = findViewById(R.id.selectcategory);
        selectCategory.setOnClickListener(v -> showCategory());
    }

    private void showCategoryDialog() {
        Category category = new Category(this);
        List<Category> categories = category.getAllCategory();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.category_list_view, null);
        ListView categoryListView = dialogView.findViewById(R.id.categorylist);
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        categoryListView.setAdapter(adapter);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Select Category")
                .setView(dialogView)
                .create();
        categoryListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedCategory = categories.get(position);
            selectCategory.setText(selectedCategory.toString());
            dialog.dismiss();
        });
        dialog.show();
        Button addCategory = dialogView.findViewById(R.id.addcategory);
        addCategory.setOnClickListener(view -> {
            dialog.dismiss();
            View createCategoryView = LayoutInflater.from(this).
                    inflate(R.layout.dialog_add_category, null);
            AlertDialog createCategoryDialog = new AlertDialog.Builder(this)
                    .setTitle("Add Category")
                    .setView(createCategoryView)
                    .create();
            createCategoryDialog.show();
            Button saveCategory = createCategoryDialog.findViewById(R.id.summitcategory);
            saveCategory.setOnClickListener(view1 -> {
                EditText newCategoryInput = createCategoryDialog.findViewById(R.id.categoryinput);
                Category newCategory = new Category(this.getBaseContext());
                newCategory.insertCategory(newCategoryInput.getText().toString(),null,auth.getUserId());
                createCategoryDialog.dismiss();
                showCategoryDialog();
            });
        });

    }


    public void showStartDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog startDatePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) ->
        {
            txtstart.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }, year, month, day);
        startDatePickerDialog.show();
    }

    public void showEndDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog EndDatePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) ->
        {
            txtend.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }, year, month, day);
        EndDatePickerDialog.show();
    }
    public void showCategory()
    {
        Category category = new Category(this);
        List<Category> categories = category.getAllCategory();
        View dialogView = LayoutInflater.from(this).inflate(R.layout.category_list_view, null);
        ListView categoryListView = dialogView.findViewById(R.id.categorylist);

        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        categoryListView.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(this)

                .setView(dialogView)
                .create();
        categoryListView.setOnItemClickListener((parent, view, position, id) ->  {
            selectedCategory = categories.get(position);
            selectCategory.setText(selectedCategory.toString());
        dialog.dismiss();
        });
        dialog.show();
        Button addCategory = dialogView.findViewById(R.id.addcategory);
        addCategory.setOnClickListener(v -> {
                    dialog.dismiss();
                    View createCategoryView = LayoutInflater.from(this).inflate(R.layout.dialog_add_category, null);

                    AlertDialog createCategoryDialog = new AlertDialog.Builder(this)

                            .setView(createCategoryView)
                            .create();
                    createCategoryDialog.show();
                    Button saveCategory = createCategoryDialog.findViewById(R.id.summitcategory);
                    saveCategory.setOnClickListener(view1 -> {
                        EditText newCategoryInput = createCategoryDialog.findViewById(R.id.categoryinput);
                        Category newCategory = new Category(this.getBaseContext());
                        newCategory.insertCategory(newCategoryInput.getText().toString(),null,auth.getUserId());
                        createCategoryDialog.dismiss();
                        showCategory();

                    });
                }
        );
    }
}