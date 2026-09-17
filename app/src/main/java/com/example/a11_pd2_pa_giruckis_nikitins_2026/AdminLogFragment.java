package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a11_pd2_pa_giruckis_nikitins_2026.databinding.AdminLogBinding;

import java.util.ArrayList;
import java.util.List;

public class AdminLogFragment extends Fragment {

    private AdminLogBinding binding;
    private List<User> userList = new ArrayList<>();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = AdminLogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.AdminBackButton.setOnClickListener(v ->
                NavHostFragment.findNavController(AdminLogFragment.this)
                        .navigate(R.id.action_AdminLogFragment_to_LoginFragment)
        );

        binding.AdminUserList.setOnItemClickListener((parent, view1, position, id) -> {
            if (position < userList.size()) {
                showEditUserDialog(userList.get(position));
            }
        });

        loadUsers();
    }

    private void showEditUserDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_user, null);
        builder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.edit_name);
        EditText editSurname = dialogView.findViewById(R.id.edit_surname);
        EditText editUsername = dialogView.findViewById(R.id.edit_username);
        EditText editEmail = dialogView.findViewById(R.id.edit_email);
        EditText editPassword = dialogView.findViewById(R.id.edit_password);

        editName.setText(user.name);
        editSurname.setText(user.surname);
        editUsername.setText(user.username);
        editEmail.setText(user.email);
        editPassword.setText(user.password);

        builder.setTitle("Edit User: " + user.username);
        builder.setPositiveButton("Update", (dialog, which) -> {
            user.name = editName.getText().toString();
            user.surname = editSurname.getText().toString();
            user.username = editUsername.getText().toString();
            user.email = editEmail.getText().toString();
            user.password = editPassword.getText().toString();
            updateUser(user);
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete " + user.username + "?")
                    .setPositiveButton("Yes", (d, w) -> deleteUser(user))
                    .setNegativeButton("No", null)
                    .show();
        });

        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    private void updateUser(User user) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            db.userDao().update(user);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "User updated", Toast.LENGTH_SHORT).show();
                    loadUsers();
                });
            }
        }).start();
    }

    private void deleteUser(User user) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            db.userDao().delete(user);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "User deleted", Toast.LENGTH_SHORT).show();
                    loadUsers();
                });
            }
        }).start();
    }

    private void loadUsers() {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            List<User> users = db.userDao().getAllUsers();
            userList = users;
            List<String> userStrings = new ArrayList<>();
            for (User user : users) {
                userStrings.add("ID: " + user.id + "\n" +
                        "Name: " + user.name + " " + user.surname + "\n" +
                        "Username: " + user.username + "\n" +
                        "Email: " + user.email);
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (getContext() != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_list_item_1, userStrings);
                        binding.AdminUserList.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
