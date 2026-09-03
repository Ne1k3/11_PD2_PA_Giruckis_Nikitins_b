package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


        binding.RegistrationRegButton.setOnClickListener(v ->
                NavHostFragment.findNavController(RegistrationFragment.this)
                        .navigate(R.id.action_RegistrationFragment_to_MainPageFragment)
        );


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


