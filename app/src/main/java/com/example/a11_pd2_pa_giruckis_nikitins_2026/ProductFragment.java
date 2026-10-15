package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.app.AlertDialog;
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
        adapter.setOnProductClickListener(this::showAddProductToListDialog);
        binding.productsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.productsRecyclerView.setAdapter(adapter);

        binding.ProductMainPageButton.setOnClickListener(v ->
                NavHostFragment.findNavController(ProductFragment.this)
                        .navigate(R.id.action_ProductFragment_to_MainPageFragment)
        );

        binding.FindProductButton.setOnClickListener(v -> filterProducts());
        binding.ProductFilterFindButton.setOnClickListener(v -> showFilterDialog());

        loadProducts();
    }

    private void showAddProductToListDialog(Product product) {
        if (getContext() == null) return;

        new AlertDialog.Builder(requireContext())
                .setMessage("Vai Jūs vēlaties pievienot šādu produktu sarakstam?")
                .setPositiveButton("Jā", (dialog, which) -> showSelectListDialog(product))
                .setNegativeButton("Nē", null)
                .show();
    }

    private void showSelectListDialog(Product product) {
        if (getContext() == null) return;

        List<String> listNames = ShoppingListManager.getListNames();
        String[] items = listNames.toArray(new String[0]);

        new AlertDialog.Builder(requireContext())
                .setTitle("Izvēlieties sarakstu")
                .setItems(items, (dialog, which) -> {
                    String selectedList = items[which];
                    ShoppingListManager.addProductToList(selectedList, product);

                    new AlertDialog.Builder(requireContext())
                            .setMessage("Produkts ir pievienots sarakstam!")
                            .setPositiveButton("OK", null)
                            .show();
                })
                .setNegativeButton("Atcelt", null)
                .show();
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

        if (filteredList.isEmpty()) {
            adapter.setProducts(filteredList);
            if (getContext() != null) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Šādu produktu neizdevās atrast")
                        .setPositiveButton("OK", null)
                        .show();
            }
        } else {
            adapter.setProducts(filteredList);
        }
    }

    private void showFilterDialog() {
        if (getContext() == null) return;

        String[] filters = {
                "Piena produkti",
                "Dārzeņi un augļi",
                "Dzērieni",
                "Bakaleja",
                "Gaļas izstrādājumi",
                "Visi produkti"
        };
        boolean[] checkedItems = new boolean[filters.length];

        new AlertDialog.Builder(requireContext())
                .setTitle("Filtri")
                .setMultiChoiceItems(filters, checkedItems, (dialog, which, isChecked) -> checkedItems[which] = isChecked)
                .setPositiveButton("OK", (dialog, which) -> applyFilter(checkedItems))
                .setNegativeButton("Atcelt", null)
                .show();
    }

    private void applyFilter(boolean[] checkedItems) {
        boolean anyChecked = false;
        for (boolean checked : checkedItems) {
            if (checked) {
                anyChecked = true;
                break;
            }
        }

        if (!anyChecked || checkedItems[5]) {
            adapter.setProducts(allProducts);
            return;
        }

        List<Product> filteredList = new ArrayList<>();
        for (Product product : allProducts) {
            String nameLower = product.name != null ? product.name.toLowerCase() : "";

            if (checkedItems[0] && (nameLower.contains("piens") || nameLower.contains("siers") || nameLower.contains("sviests"))) {
                if (!filteredList.contains(product)) filteredList.add(product);
            }
            if (checkedItems[1] && (nameLower.contains("āboli") || nameLower.contains("aboli") || nameLower.contains("banāni") || nameLower.contains("banani") || nameLower.contains("tomāti") || nameLower.contains("tomati") || nameLower.contains("gurķi") || nameLower.contains("gurki"))) {
                if (!filteredList.contains(product)) filteredList.add(product);
            }
            if (checkedItems[2] && (nameLower.contains("kafija") || nameLower.contains("tēja") || nameLower.contains("teja") || nameLower.contains("sula") || nameLower.contains("ūdens") || nameLower.contains("udens"))) {
                if (!filteredList.contains(product)) filteredList.add(product);
            }
            if (checkedItems[3] && (nameLower.contains("maize") || nameLower.contains("olas") || nameLower.contains("cukurs") || nameLower.contains("milti"))) {
                if (!filteredList.contains(product)) filteredList.add(product);
            }
        }

        if (filteredList.isEmpty()) {
            adapter.setProducts(filteredList);
            if (getContext() != null) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Šādu produktu neizdevās atrast")
                        .setPositiveButton("OK", null)
                        .show();
            }
        } else {
            adapter.setProducts(filteredList);
        }
    }

    private void loadProducts() {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            List<Product> products = db.productDao().getAllProducts();

            // Удаляем автоматически созданные ранее тестовые продукты "Produkt 1"..."Produkt 6"
            boolean cleaned = false;
            for (Product p : new ArrayList<>(products)) {
                if (p.name != null && p.name.matches("Produkt [1-6]") && (p.imageUri == null || p.imageUri.isEmpty())) {
                    db.productDao().delete(p);
                    cleaned = true;
                }
            }
            if (cleaned) {
                products = db.productDao().getAllProducts();
            }

            if (products.isEmpty()) {
                db.productDao().insert(new Product("Piens", ""));
                db.productDao().insert(new Product("Maize", ""));
                db.productDao().insert(new Product("Siers", ""));
                db.productDao().insert(new Product("Āboli", ""));
                db.productDao().insert(new Product("Banāni", ""));
                db.productDao().insert(new Product("Olas", ""));
                db.productDao().insert(new Product("Sviests", ""));
                db.productDao().insert(new Product("Kafija", ""));
                db.productDao().insert(new Product("Tēja", ""));
                db.productDao().insert(new Product("Tomāti", ""));
                db.productDao().insert(new Product("Gurķi", ""));
                db.productDao().insert(new Product("Sula", ""));
                db.productDao().insert(new Product("Ūdens", ""));
                db.productDao().insert(new Product("Cukurs", ""));
                db.productDao().insert(new Product("Milti", ""));
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
