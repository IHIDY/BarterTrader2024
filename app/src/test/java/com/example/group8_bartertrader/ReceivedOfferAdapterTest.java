package com.example.group8_bartertrader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import com.example.group8_bartertrader.adapter.ReceivedOfferAdapter;
import com.example.group8_bartertrader.model.Offer;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

public class ReceivedOfferAdapterTest {

    private ReceivedOfferAdapter adapter;
    private List<Offer> testOffers;
    private Context context;

    @Before
    public void setUp(){
        testOffers = new ArrayList<>();
        Offer offer = new Offer();
        offer.setId("1");
        offer.setOfferedItemName("Laptop");
        offer.setStatus("Pending");
        testOffers.add(offer);

        Offer offer2 = new Offer();
        offer2.setId("2");
        offer2.setOfferedItemName("Tablet");
        offer2.setStatus("Accepted");
        testOffers.add(offer2);

        adapter = new ReceivedOfferAdapter(testOffers, context, null);
    }

    @Test
    public void itemCountTest(){
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void offerStatusTest(){
        assertEquals("Pending", testOffers.get(0).getStatus());
        assertEquals("Accepted", testOffers.get(1).getStatus());
    }

    @Test
    public void offerListNotEmptyTest(){
        assertFalse(testOffers.isEmpty());
    }

    @Test
    public void offerIdNotNullTest(){
        assertNotNull(testOffers.get(0).getId());
        assertNotNull(testOffers.get(1).getId());
    }
}
