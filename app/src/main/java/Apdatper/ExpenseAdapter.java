package Apdatper;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ameproject.R;

import java.util.Calendar;
import java.util.List;

import Model.Category;
import Model.Expense;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private final List<Expense> expenses;

    public ExpenseAdapter(List<Expense> expense) {
        this.expenses = expense;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.amountText.setText("Amount: $" + expense.getAmount());
        holder.categoryText.setText("Category: " + expense.getCategoryName());
        holder.noteText.setText("Note: " + expense.getNote());
        holder.endDateTextView.setText("Date: " + expense.getDate());
        holder.itemView.setOnClickListener(view ->
        {
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_update_expense, null);
            AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                    .setTitle("Update Expense")
                    .setView(dialogView)
                    .setPositiveButton("Update", null)
                    .setNegativeButton("Canncel",(dialogInterface,i) -> dialogInterface.dismiss())
                    .create();
            EditText amountInput = dialogView.findViewById(R.id.editAmount);
            EditText noteInput = dialogView.findViewById(R.id.editNote);
            EditText dateInput = dialogView.findViewById(R.id.editDate);
            TextView selectCategory = dialogView.findViewById(R.id.editCategory);

            amountInput.setText(String.valueOf(expense.getAmount()));
            noteInput.setText(expense.getNote());
            dateInput.setText(expense.getDate());
            selectCategory.setText(expense.getCategoryName());

            selectCategory.setOnClickListener(v ->{
                Category category = new Category(view.getContext());
                List<Category> categories = category.getAllCategory();
                View categoryDialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.category_list_view,null);
                ListView categoryListView = categoryDialogView.findViewById(R.id.categorylist);

                CategoryAdapter adapter = new CategoryAdapter(view.getContext(),categories);
                categoryListView.setAdapter(adapter);

                AlertDialog cateDialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Select Category")
                        .setView(categoryDialogView)
                        .create();

                categoryListView.setOnItemClickListener((parent, categoryView, categoryPostion, id) ->
                {
                    Category selectedCategory = categories.get(categoryPostion);
                    selectCategory.setText(selectedCategory.toString());
                    expense.setCategoryId(selectedCategory.getId());
                    expense.setCategoryName(selectedCategory.getName());
                    cateDialog.dismiss();
                });
                cateDialog.show();
            });
            dateInput.setOnTouchListener((v, motionEvent)->{
                if(motionEvent.getAction()== MotionEvent.ACTION_UP)
                {
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                    (datePicker, selectedYear, selectedMonth, selectedDay) ->
                    {
                        String selectedDate = selectedDay + "/" + (selectedMonth+1) + "/" + selectedYear;
                        dateInput.setText(selectedDate);
                        expense.setDate(selectedDate);
                    } , year, month, day);
            datePickerDialog.show();

                }
                return true;
            });

            dialog.setOnShowListener(dialogInterface ->
            {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v ->{
                    String newAmout = amountInput.getText().toString().trim();
                    String newNote = noteInput.getText().toString().trim();

                    Expense newExpense = new Expense(view.getContext());
                    newExpense.setId(expense.getId());
                    newExpense.setCategoryId(expense.getCategoryId());
                    newExpense.setDate(expense.getDate());
                    newExpense.setAmount(Integer.parseInt(newAmout));
                    newExpense.setNote(newNote);
                    newExpense.setCategoryName(expense.getCategoryName());
                    newExpense.updateExpense();
                    expenses.set(position, newExpense);
                    notifyItemChanged(position);
                    dialog.dismiss();

                });
            });
dialog.show();
        });

    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }


    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView amountText, categoryText, noteText, remainingText, endDateTextView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            amountText = itemView.findViewById(R.id.amoutText);
            remainingText = itemView.findViewById(R.id.remainingText);
            categoryText = itemView.findViewById(R.id.categoryText);
            noteText = itemView.findViewById(R.id.noteText);

            endDateTextView = itemView.findViewById(R.id.enddateText);
        }
    }

}
