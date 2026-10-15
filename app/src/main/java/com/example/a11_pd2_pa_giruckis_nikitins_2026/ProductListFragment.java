package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a11_pd2_pa_giruckis_nikitins_2026.databinding.ProductListBinding;

import java.util.List;

public class ProductListFragment extends Fragment {
    private ProductListBinding binding;
    private boolean isDeleteMode = false;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = ProductListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.ListMainPageButton.setOnClickListener(v ->
                NavHostFragment.findNavController(ProductListFragment.this)
                        .navigate(R.id.action_ProductListFragment_to_MainPageFragment)
        );

        binding.ListDeleteProductButton.setOnClickListener(v -> {
            List<Product> list = ShoppingListManager.getCurrentList();
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "Saraksts ir tukšs", Toast.LENGTH_SHORT).show();
            } else {
                isDeleteMode = !isDeleteMode;
                updateListUI();
                if (isDeleteMode) {
                    Toast.makeText(getContext(), "Uzspiediet uz krustiņu, lai dzēstu produktu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateListUI();
    }

    private void updateListUI() {
        if (binding == null) return;

        binding.ListText.setText(ShoppingListManager.getCurrentSelectedListName());

        List<Product> products = ShoppingListManager.getCurrentList();

        ImageView[] imageViews = {
                binding.ListView1, binding.ListView2, binding.ListView3,
                binding.ListView4, binding.ListView5, binding.ListView6
        };
        TextView[] textViews = {
                binding.ListTextView1, binding.ListTextView2, binding.ListTextView3,
                binding.ListTextView4, binding.ListTextView5, binding.ListTextView6
        };
        ImageView[] crossViews = {
                binding.ListCross1, binding.ListCross2, binding.ListCross3,
                binding.ListCross4, binding.ListCross5, binding.ListCross6
        };

        for (int i = 0; i < 6; i++) {
            if (i < products.size()) {
                Product p = products.get(i);
                imageViews[i].setVisibility(View.VISIBLE);
                textViews[i].setVisibility(View.VISIBLE);

                // Отображаем крестики ТОЛЬКО когда нажата кнопка "Dzēst Produktu"
                crossViews[i].setVisibility(isDeleteMode ? View.VISIBLE : View.GONE);

                textViews[i].setText(p.name);
                if (p.imageUri != null && !p.imageUri.isEmpty()) {
                    try {
                        imageViews[i].setImageURI(Uri.parse(p.imageUri));
                    } catch (Exception e) {
                        imageViews[i].setImageResource(R.drawable.ic_launcher_foreground);
                    }
                } else {
                    imageViews[i].setImageResource(R.drawable.ic_launcher_foreground);
                }

                final int index = i;
                crossViews[i].setOnClickListener(v -> deleteProductAt(index));
            } else {
                imageViews[i].setVisibility(View.GONE);
                textViews[i].setVisibility(View.GONE);
                crossViews[i].setVisibility(View.GONE);
            }
        }
    }

    private void deleteProductAt(int index) {
        if (index >= 0 && index < ShoppingListManager.getCurrentList().size()) {
            ShoppingListManager.removeProductFromCurrentList(index);
            isDeleteMode = false; // Выходим из режима удаления
            updateListUI();

            if (getContext() != null) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Produkts ir dzest no sarakstu!")
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
