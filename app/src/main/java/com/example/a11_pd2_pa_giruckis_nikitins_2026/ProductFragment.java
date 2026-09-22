package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.a11_pd2_pa_giruckis_nikitins_2026.databinding.ProductsBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {
    private ProductsBinding binding;
    private ProductAdapter adapter;
    private List<Product> allProducts = new ArrayList<>();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = ProductsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ProductAdapter();
        binding.productsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.productsRecyclerView.setAdapter(adapter);

        binding.ProductMainPageButton.setOnClickListener(v ->
                NavHostFragment.findNavController(ProductFragment.this)
                        .navigate(R.id.action_ProductFragment_to_MainPageFragment)
        );

        binding.FindProductButton.setOnClickListener(v -> filterProducts());

        loadProducts();
    }

    private void filterProducts() {
        String searchQuery = binding.ProductFindInput.getText().toString().trim().toLowerCase();
        if (searchQuery.isEmpty() || searchQuery.equals("name")) {
            adapter.setProducts(allProducts);
            return;
        }

        List<Product> filteredList = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.name != null && product.name.toLowerCase().contains(searchQuery)) {
                filteredList.add(product);
            }
        }
        adapter.setProducts(filteredList);
    }

    private void loadProducts() {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            List<Product> products = db.productDao().getAllProducts();
            if (products.isEmpty()) {
                // Seed initial sample products if DB is empty
                for (int i = 1; i <= 6; i++) {
                    Product sample = new Product("Produkt " + i, "");
                    db.productDao().insert(sample);
                }
                products = db.productDao().getAllProducts();
            }

            allProducts = products;
            List<Product> finalProducts = products;

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> adapter.setProducts(finalProducts));
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
