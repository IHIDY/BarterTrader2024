package com.example.group8_bartertrader.CRUD;

import com.example.group8_bartertrader.R;
import com.example.group8_bartertrader.model.Product;

import java.util.ArrayList;

public class ProductCRUD {
    ArrayList<Product> products;

    public ProductCRUD() {
        this.products = new ArrayList<>();
        this.loadSampleProds();
    }

    protected void loadSampleProds() {
        Product prod1 = new Product("1", "Pink Vase", "Good Condition",
                "Home Decor", true, "", "Halifax");
        Product prod2 = new Product("2", "Computer Mouse", "Fair Condition",
                "Electronics", true, "", "Halifax");
        Product prod3 = new Product("3", "Star Wars Figurine", "Fair Condition",
                "Collectibles", true, "", "Halifax");

        products.add(prod1);
        products.add(prod2);
        products.add(prod3);
    }

    public ArrayList<Product> collectProducts() {
        return products;
    }

    public Product deliverTopProduct(ArrayList<Product> products) {
        if (!products.isEmpty()) {
            Product topProd = products.get(0);
            products.remove(topProd);
            return topProd;
        } else return null;
    }
}
