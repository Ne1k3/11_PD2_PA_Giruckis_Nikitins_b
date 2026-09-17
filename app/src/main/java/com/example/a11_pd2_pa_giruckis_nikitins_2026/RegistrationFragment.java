package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a11_pd2_pa_giruckis_nikitins_2026.databinding.RegistrationBinding;

public class RegistrationFragment extends Fragment {

    private RegistrationBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = RegistrationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.RegistrationRegButton.setOnClickListener(v -> {
            String name = binding.editTextText.getText().toString().trim();
            String surname = binding.editTextText2.getText().toString().trim();
            String username = binding.editTextText3.getText().toString().trim();
            String email = binding.editTextTextEmailAddress.getText().toString().trim();
            String password = binding.editTextTextPassword.getText().toString();
            String confirmPassword = binding.editTextTextPassword2.getText().toString();

            if (name.isEmpty() || surname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase db = AppDatabase.getDatabase(getContext());
            new Thread(() -> {
                User existingUser = db.userDao().findByUsername(username);
                if (existingUser != null) {
                    getActivity().runOnUiThread(() -> 
                        Toast.makeText(getContext(), "Username already exists", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    User newUser = new User(name, surname, username, email, password);
                    db.userDao().insert(newUser);
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(RegistrationFragment.this)
                                .navigate(R.id.action_RegistrationFragment_to_MainPageFragment);
                    });
                }
            }).start();
        });

        binding.RegistrationSignInButton.setOnClickListener(v ->
                NavHostFragment.findNavController(RegistrationFragment.this)
                        .navigate(R.id.action_RegistrationFragment_to_LoginFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
