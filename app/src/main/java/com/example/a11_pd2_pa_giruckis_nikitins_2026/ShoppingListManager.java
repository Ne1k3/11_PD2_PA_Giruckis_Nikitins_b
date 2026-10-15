package com.example.a11_pd2_pa_giruckis_nikitins_2026;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingListManager {
    private static final List<String> listNames = new ArrayList<>();
    private static final Map<String, List<Product>> listsMap = new HashMap<>();
    private static String currentSelectedListName = "Saraksts 1";

    static {
        addList("Saraksts 1");
        addList("Saraksts 2");
        addList("Saraksts 3");
        addList("Saraksts 4");


        addProductToList("Saraksts 1", new Product("Piens", ""));
        addProductToList("Saraksts 1", new Product("Maize", ""));
        addProductToList("Saraksts 1", new Product("Siers", ""));
    }

    public static List<String> getListNames() {
        return listNames;
    }

    public static void addList(String name) {
        if (name != null && !name.trim().isEmpty() && !listsMap.containsKey(name)) {
            listNames.add(name);
            listsMap.put(name, new ArrayList<>());
        }
    }

    public static String getCurrentSelectedListName() {
        return currentSelectedListName;
    }

    public static void setCurrentSelectedListName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            currentSelectedListName = name;
            if (!listsMap.containsKey(name)) {
                addList(name);
            }
        }
    }

    public static List<Product> getCurrentList() {
        return getListByName(currentSelectedListName);
    }

    public static List<Product> getListByName(String name) {
        if (!listsMap.containsKey(name)) {
            addList(name);
        }
        return listsMap.get(name);
    }

    public static void addProductToList(String listName, Product product) {
        List<Product> products = getListByName(listName);
        if (product != null) {
            products.add(product);
        }
    }

    public static void removeProductFromCurrentList(int index) {
        List<Product> products = getCurrentList();
        if (index >= 0 && index < products.size()) {
            products.remove(index);
        }
    }
}
