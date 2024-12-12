package com.example.ameproject;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.List;

import Apdatper.CategoryAdapter;
import Model.Category;
import Model.Expense;


public class AddFragment extends Fragment {
private EditText editTextDate;
private TextView selectCategory;
public Category selectedCategory;

Auth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = new Auth(this.getContext());
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        editTextDate = view.findViewById(R.id.dateinput);
        editTextDate.setOnTouchListener((view1, motionEvent) ->
        {
          if(motionEvent.getAction()== MotionEvent.ACTION_UP)
          {
              showDatePickerDialog();
          }
          return true;
        });
        selectCategory = view.findViewById(R.id.selectcategory);
        selectCategory.setOnClickListener(view1 ->
        {
            showCategory();
        });
        Button saveExpense = view.findViewById(R.id.saveExpense);
        saveExpense.setOnClickListener(v ->
        {
            EditText amount = view.findViewById(R.id.txtplain);
            EditText note = view.findViewById(R.id.noteinput);
            Expense expense = new Expense(this.getContext());
            expense.insertExpense(Integer.valueOf(amount.getText().toString()),
                    selectedCategory.getId(),
                    auth.getUserId(),
                    editTextDate.getText().toString(),
                    note.getText().toString()
            );
            Toast.makeText(this.getContext(),"Created Complete", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this.getContext(),MainActivity.class);
            this.getContext().startActivity(intent);

        });
        {
            return view;
        }



    }
    public void showCategory()
    {
        Category category = new Category(getContext());
        List<Category> categories = category.getAllCategory();
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.category_list_view, null);
        ListView categoryListView = dialogView.findViewById(R.id.categorylist);

        CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);
        categoryListView.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(getContext())

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
                    View createCategoryView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_category, null);

                    AlertDialog createCategoryDialog = new AlertDialog.Builder(getContext())

                            .setView(createCategoryView)
                            .create();
                    createCategoryDialog.show();
                    Button saveCategory = createCategoryDialog.findViewById(R.id.summitcategory);
                    saveCategory.setOnClickListener(view1 -> {
                        EditText newCategoryInput = createCategoryDialog.findViewById(R.id.categoryinput);
                        Category newCategory = new Category(this.getContext());
                        newCategory.insertCategory(newCategoryInput.getText().toString(),null,auth.getUserId());
                        createCategoryDialog.dismiss();
                        showCategory();

                    });
                }
        );
    }
    public void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog startDatePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) ->
        {
            editTextDate.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }, year, month, day);
        startDatePickerDialog.show();
    }
}

