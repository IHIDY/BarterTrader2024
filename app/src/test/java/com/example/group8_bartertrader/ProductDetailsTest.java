package com.example.group8_bartertrader;

import com.example.group8_bartertrader.CRUD.ProductCRUD;
import com.example.group8_bartertrader.model.Product;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class ProductDetailsTest {
    ProductCRUD crud;
    ArrayList<Product> products;

    @Before
    public void setup() {
        crud = new ProductCRUD();
        products = crud.collectProducts();
    }

    @Test
    public void testProductName() {
        Product firstProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Pink Vase", firstProd.getName());
        Product secondProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Peach Bag", secondProd.getName());
        Product thirdProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Black Sunglasses", thirdProd.getName());
    }

    @Test
    public void testProductCategory() {
        Product firstProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Electronic", firstProd.getCategory());
        Product secondProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Fashion", secondProd.getCategory());
        Product thirdProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Accesories", thirdProd.getCategory());
    }

    @Test
    public void testProductDescription() {
        Product firstProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Home Decor for Living room", firstProd.getDescription());
        Product secondProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Nice Bag", secondProd.getDescription());
        Product thirdProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Fashionable Sunglasses", thirdProd.getDescription());
    }

    @Test
    public void testProductLocation() {
        Product firstProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Halifax", firstProd.getLocation());
        Product secondProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Halifax", secondProd.getLocation());
        Product thirdProd = crud.deliverTopProduct(products);
        Assert.assertEquals("Halifax", thirdProd.getLocation());
    }
}
