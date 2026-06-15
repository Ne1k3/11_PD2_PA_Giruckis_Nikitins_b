package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a11_pd2_pa_giruckis_nikitins_2026.databinding.ContentMainBinding;


public class MainPageFragment extends Fragment {

    private ContentMainBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ContentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button ProductButton = view.findViewById(R.id.action_RegistrationFragment_to_LoginFragment);

        ProductButton.setOnClickListener(v ->
                NavHostFragment.findNavController(MainPageFragment.this)
                        .navigate(R.id.action_RegistrationFragment_to_LoginFragment)
        );
        Button SingInButton = view.findViewById(R.id.Registration_SignIn_Button);

        SingInButton.setOnClickListener(v ->
                NavHostFragment.findNavController(MainPageFragment.this)
                        .navigate(R.id.action_RegistrationFragment_to_LoginFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}