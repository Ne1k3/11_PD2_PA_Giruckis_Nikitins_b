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

        Button ProductButton = view.findViewById(R.id.main_page_newList_button);

        ProductButton.setOnClickListener(v ->
                NavHostFragment.findNavController(MainPageFragment.this)
                        .navigate(R.id.action_MainPageFragment_to_ProductFragment)
        );

        Button PoductList1Button = view.findViewById(R.id.main_page_list1);

        PoductList1Button.setOnClickListener(v ->
                NavHostFragment.findNavController(MainPageFragment.this)
                        .navigate(R.id.action_MainPageFragment_to_ProductListFragment)
        );

        Button PoductList2Button = view.findViewById(R.id.main_page_list2);

        PoductList2Button.setOnClickListener(v ->
                NavHostFragment.findNavController(MainPageFragment.this)
                        .navigate(R.id.action_MainPageFragment_to_ProductListFragment)
        );

        Button PoductList3Button = view.findViewById(R.id.main_page_list3);

        PoductList3Button.setOnClickListener(v ->
                NavHostFragment.findNavController(MainPageFragment.this)
                        .navigate(R.id.action_MainPageFragment_to_ProductListFragment)
        );

        Button PoductList4Button = view.findViewById(R.id.main_page_list4);

        PoductList4Button.setOnClickListener(v ->
                NavHostFragment.findNavController(MainPageFragment.this)
                        .navigate(R.id.action_MainPageFragment_to_ProductListFragment)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}