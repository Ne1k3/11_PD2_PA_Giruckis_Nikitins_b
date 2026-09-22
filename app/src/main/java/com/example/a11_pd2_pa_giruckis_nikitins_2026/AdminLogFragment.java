package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a11_pd2_pa_giruckis_nikitins_2026.databinding.AdminLogBinding;

import java.util.ArrayList;
import java.util.List;

public class AdminLogFragment extends Fragment {

    private AdminLogBinding binding;
    private List<User> userList = new ArrayList<>();

    private Uri selectedImageUri = null;
    private ImageView currentDialogImageView = null;

    private final ActivityResultLauncher<String> selectImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    try {
                        requireContext().getContentResolver().takePersistableUriPermission(
                                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (Exception ignored) {
                    }
                    if (currentDialogImageView != null) {
                        currentDialogImageView.setImageURI(uri);
                    }
                }
            });

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = AdminLogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.AdminBackButton.setOnClickListener(v ->
                NavHostFragment.findNavController(AdminLogFragment.this)
                        .navigate(R.id.action_AdminLogFragment_to_LoginFragment)
        );

        binding.AdminAddProductButton.setOnClickListener(v -> showAddProductDialog());
        binding.AdminDeleteProductButton.setOnClickListener(v -> showDeleteProductDialog());

        binding.AdminUserList.setOnItemClickListener((parent, view1, position, id) -> {
            if (position < userList.size()) {
                showEditUserDialog(userList.get(position));
            }
        });

        loadUsers();
    }

    private void showEditUserDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_user, null);
        builder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.edit_name);
        EditText editSurname = dialogView.findViewById(R.id.edit_surname);
        EditText editUsername = dialogView.findViewById(R.id.edit_username);
        EditText editEmail = dialogView.findViewById(R.id.edit_email);
        EditText editPassword = dialogView.findViewById(R.id.edit_password);

        editName.setText(user.name);
        editSurname.setText(user.surname);
        editUsername.setText(user.username);
        editEmail.setText(user.email);
        editPassword.setText(user.password);

        builder.setTitle("Edit User: " + user.username);
        builder.setPositiveButton("Update", (dialog, which) -> {
            user.name = editName.getText().toString();
            user.surname = editSurname.getText().toString();
            user.username = editUsername.getText().toString();
            user.email = editEmail.getText().toString();
            user.password = editPassword.getText().toString();
            updateUser(user);
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete " + user.username + "?")
                    .setPositiveButton("Yes", (d, w) -> deleteUser(user))
                    .setNegativeButton("No", null)
                    .show();
        });

        builder.setNeutralButton("Cancel", null);
        builder.show();
    }

    private void updateUser(User user) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            db.userDao().update(user);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "User updated", Toast.LENGTH_SHORT).show();
                    loadUsers();
                });
            }
        }).start();
    }

    private void deleteUser(User user) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            db.userDao().delete(user);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "User deleted", Toast.LENGTH_SHORT).show();
                    loadUsers();
                });
            }
        }).start();
    }

    private void showAddProductDialog() {
        if (getContext() == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.edit_product_name);
        currentDialogImageView = dialogView.findViewById(R.id.image_product_preview);
        Button btnSelectImage = dialogView.findViewById(R.id.btn_select_image);

        selectedImageUri = null;

        btnSelectImage.setOnClickListener(v -> selectImageLauncher.launch("image/*"));

        builder.setTitle(R.string.AddProduct_Title);
        builder.setPositiveButton(R.string.AddProduct_AddButton, (dialog, which) -> {
            String productName = editName.getText().toString().trim();
            if (productName.isEmpty()) {
                Toast.makeText(getContext(), "Lūdzu ievadiet produkta nosaukumu", Toast.LENGTH_SHORT).show();
                return;
            }

            String imageUriStr = selectedImageUri != null ? selectedImageUri.toString() : "";
            saveProduct(productName, imageUriStr);
        });

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    private void saveProduct(String name, String imageUri) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            db.productDao().insert(new Product(name, imageUri));
            if (getActivity() != null) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), R.string.AddProduct_Success, Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void showDeleteProductDialog() {
        if (getContext() == null) return;
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            List<Product> products = db.productDao().getAllProducts();
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                if (products.isEmpty()) {
                    Toast.makeText(getContext(), R.string.DeleteProduct_Empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] productNames = new String[products.size()];
                for (int i = 0; i < products.size(); i++) {
                    productNames[i] = products.get(i).name;
                }

                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.DeleteProduct_Title)
                        .setItems(productNames, (dialog, which) -> {
                            Product selectedProduct = products.get(which);
                            confirmDeleteProduct(selectedProduct);
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            });
        }).start();
    }

    private void confirmDeleteProduct(Product product) {
        if (getContext() == null) return;
        String message = String.format(getString(R.string.DeleteProduct_Confirm), product.name);
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.AdminLog_DeleteProduct)
                .setMessage(message)
                .setPositiveButton("Dzēst", (dialog, which) -> deleteProduct(product))
                .setNegativeButton("Atcelt", null)
                .show();
    }

    private void deleteProduct(Product product) {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            db.productDao().delete(product);
            if (getActivity() != null) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), R.string.DeleteProduct_Success, Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void loadUsers() {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            List<User> users = db.userDao().getAllUsers();
            userList = users;
            List<String> userStrings = new ArrayList<>();
            for (User user : users) {
                userStrings.add("ID: " + user.id + "\n" +
                        "Name: " + user.name + " " + user.surname + "\n" +
                        "Username: " + user.username + "\n" +
                        "Email: " + user.email);
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (getContext() != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_list_item_1, userStrings);
                        binding.AdminUserList.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
