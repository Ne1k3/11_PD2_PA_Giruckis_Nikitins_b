package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a11_pd2_pa_giruckis_nikitins_2026.databinding.LoginBinding;

public class LoginFragment extends Fragment {

    private LoginBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = LoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.LoginRegistration.setOnClickListener(v ->
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_FirstFragment)
        );

        binding.LoginSignIn.setOnClickListener(v -> {
            String username = binding.LoginUsernameInput.getText().toString().trim();
            String password = binding.LoginPasswordInput.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check for admin login
            if (username.equals("admin") && password.equals("admin")) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_AdminLogFragment);
                return;
            }

            AppDatabase db = AppDatabase.getDatabase(getContext());
            new Thread(() -> {
                User user = db.userDao().login(username, password);
                getActivity().runOnUiThread(() -> {
                    if (user != null) {
                        Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(LoginFragment.this)
                                .navigate(R.id.action_LoginFragment_to_MainPageFragment);
                    } else {
                        Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
