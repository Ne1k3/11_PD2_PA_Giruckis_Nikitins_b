package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a11_pd2_pa_giruckis_nikitins_2026.databinding.MainPageBinding;

import java.util.List;

public class MainPageFragment extends Fragment {

    private MainPageBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = MainPageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Кнопка "Izveidot jaunu sarakstu" открывает диалог создания нового списка
        Button newListButton = view.findViewById(R.id.main_page_newList_button);
        if (newListButton != null) {
            newListButton.setOnClickListener(v -> showCreateListDialog());
        }

        // Кнопка "Pievienot produktu sarakstam" перекидывает на список продуктов (ProductFragment)
        Button addProductButton = view.findViewById(R.id.main_page_addProduct_button);
        if (addProductButton != null) {
            addProductButton.setOnClickListener(v ->
                    NavHostFragment.findNavController(MainPageFragment.this)
                            .navigate(R.id.action_MainPageFragment_to_ProductFragment)
            );
        }

        // Поиск списков по названию
        binding.mainPageSearchButton.setOnClickListener(v -> filterLists());

        // Существующие кнопки списков
        Button poductList1Button = view.findViewById(R.id.main_page_list1);
        if (poductList1Button != null) {
            poductList1Button.setOnClickListener(v -> openList("Saraksts 1"));
        }

        Button poductList2Button = view.findViewById(R.id.main_page_list2);
        if (poductList2Button != null) {
            poductList2Button.setOnClickListener(v -> openList("Saraksts 2"));
        }

        Button poductList3Button = view.findViewById(R.id.main_page_list3);
        if (poductList3Button != null) {
            poductList3Button.setOnClickListener(v -> openList("Saraksts 3"));
        }

        Button poductList4Button = view.findViewById(R.id.main_page_list4);
        if (poductList4Button != null) {
            poductList4Button.setOnClickListener(v -> openList("Saraksts 4"));
        }

        loadCustomLists();
    }

    private void filterLists() {
        if (binding == null) return;
        String query = binding.mainPageSearch.getText().toString().trim().toLowerCase();
        if (query.equals("0")) {
            query = "";
        }

        LinearLayout container = binding.mainPageListsContainer;
        if (container == null) return;

        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof Button) {
                Button btn = (Button) child;
                String text = btn.getText().toString().toLowerCase();
                if (query.isEmpty() || text.contains(query)) {
                    btn.setVisibility(View.VISIBLE);
                } else {
                    btn.setVisibility(View.GONE);
                }
            }
        }
    }

    private void loadCustomLists() {
        List<String> allLists = ShoppingListManager.getListNames();
        for (String listName : allLists) {
            if (listName.equals("Saraksts 1") || listName.equals("Saraksts 2") ||
                    listName.equals("Saraksts 3") || listName.equals("Saraksts 4")) {
                continue;
            }
            boolean exists = false;
            LinearLayout container = binding.mainPageListsContainer;
            if (container != null) {
                for (int i = 0; i < container.getChildCount(); i++) {
                    View child = container.getChildAt(i);
                    if (child instanceof Button && ((Button) child).getText().toString().equals(listName)) {
                        exists = true;
                        break;
                    }
                }
            }
            if (!exists) {
                addListButtonToContainer(listName);
            }
        }
    }

    private void openList(String listName) {
        ShoppingListManager.setCurrentSelectedListName(listName);
        NavHostFragment.findNavController(MainPageFragment.this)
                .navigate(R.id.action_MainPageFragment_to_ProductListFragment);
    }

    private void showCreateListDialog() {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.create_list_dialog_title);

        final EditText input = new EditText(requireContext());
        input.setHint(R.string.create_list_dialog_title);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        input.setPadding(padding, padding, padding, padding);
        builder.setView(input);

        builder.setPositiveButton(R.string.create_list_dialog_positive, (dialog, which) -> {
            String listName = input.getText().toString().trim();
            if (listName.isEmpty()) {
                listName = "Jauns saraksts";
            }
            ShoppingListManager.addList(listName);
            addListButtonToContainer(listName);

            new AlertDialog.Builder(requireContext())
                    .setMessage(R.string.create_list_success)
                    .setPositiveButton("OK", null)
                    .show();
        });

        builder.setNegativeButton(R.string.create_list_dialog_negative, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addListButtonToContainer(String listName) {
        if (binding == null) return;

        LinearLayout container = binding.mainPageListsContainer;
        if (container == null) return;

        Button newButton = new Button(requireContext());
        newButton.setText(listName);
        newButton.setTextSize(20);
        newButton.setTypeface(null, Typeface.ITALIC);
        newButton.setBackgroundTintList(ColorStateList.valueOf(0xFFEFA441));

        int widthPx = (int) (273 * getResources().getDisplayMetrics().density);
        int heightPx = (int) (59 * getResources().getDisplayMetrics().density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPx, heightPx);
        int marginPx = (int) (12 * getResources().getDisplayMetrics().density);
        params.setMargins(0, marginPx, 0, marginPx);
        newButton.setLayoutParams(params);

        final String name = listName;
        newButton.setOnClickListener(v -> openList(name));

        container.addView(newButton);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
