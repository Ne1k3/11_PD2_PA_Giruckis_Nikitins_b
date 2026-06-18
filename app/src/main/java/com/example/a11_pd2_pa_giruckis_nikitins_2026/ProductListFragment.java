package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a11_pd2_pa_giruckis_nikitins_2026.databinding.ProductListBinding;

public class ProductListFragment extends Fragment {
    private ProductListBinding binding;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = ProductListBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button ProductButton = view.findViewById(R.id.List_MainPage_Button);

        ProductButton.setOnClickListener(v ->
                NavHostFragment.findNavController(ProductListFragment.this)
                        .navigate(R.id.action_ProductListFragment_to_MainPageFragment)
        );
    }
}
