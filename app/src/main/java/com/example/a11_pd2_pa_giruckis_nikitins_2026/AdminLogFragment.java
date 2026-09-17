package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a11_pd2_pa_giruckis_nikitins_2026.databinding.AdminLogBinding;

import java.util.ArrayList;
import java.util.List;

public class AdminLogFragment extends Fragment {

    private AdminLogBinding binding;

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

        loadUsers();
    }

    private void loadUsers() {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            List<User> users = db.userDao().getAllUsers();
            List<String> userStrings = new ArrayList<>();
            for (User user : users) {
                userStrings.add("ID: " + user.id + "\n" +
                        "Name: " + user.name + " " + user.surname + "\n" +
                        "Username: " + user.username + "\n" +
                        "Email: " + user.email);
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_list_item_1, userStrings);
                    binding.AdminUserList.setAdapter(adapter);
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
