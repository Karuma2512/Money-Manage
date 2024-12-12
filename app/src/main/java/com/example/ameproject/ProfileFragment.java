package com.example.ameproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import Model.Users;

public class ProfileFragment extends Fragment {
    TextView ame;
    ImageButton logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView textProfile = view.findViewById(R.id.profilename);
        Auth auth = new Auth(getContext());
        textProfile.setText(auth.getName());

        // Nút Editname
        ImageButton editNameButton = view.findViewById(R.id.editName);
        editNameButton.setOnClickListener(w -> {
            // Tạo AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater dialogInflater = getLayoutInflater();
            View dialogView = dialogInflater.inflate(R.layout.edit_name, null);

            // Lấy EditText từ layout edit_name
            EditText editNameField = dialogView.findViewById(R.id.editName);

            builder.setView(dialogView)
                    .setTitle("Edit Name")
                    .setPositiveButton("Save", null) // Chúng ta sẽ gán OnClickListener sau
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog alertDialog = builder.create();

            // Xử lý sự kiện khi nhấn nút Save
            alertDialog.setOnShowListener(dialogInterface -> {
                Button saveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                saveButton.setOnClickListener(v -> {
                    String newName = editNameField.getText().toString().trim();
                    if (newName.isEmpty()) {
                        Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        // Cập nhật tên trong cơ sở dữ liệu
                        Users user = new Users(getContext());
                        String currentName = auth.getName(); // Lấy tên hiện tại từ Auth
                        user.editName(currentName, newName);

                        // Cập nhật tên trong TextView
                        textProfile.setText(newName);

                        // Lưu lại tên mới vào Auth
                        auth.setName(newName);

                        Toast.makeText(getContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
            });

            alertDialog.show();
        });

        // Nút Logout
        logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(v -> {
            auth.logout();
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            view.getContext().startActivity(intent);
        });

        return view;
    }

}