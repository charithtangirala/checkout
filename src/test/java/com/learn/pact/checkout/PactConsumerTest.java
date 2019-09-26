package com.learn.pact.checkout;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PactConsumerTest {
    @Rule
    public PactProviderRuleMk2 pactProviderRuleMk2 = new PactProviderRuleMk2("inventory", "localhost", 8080, this);

    @Pact(provider = "inventory", consumer = "checkout")
    public RequestResponsePact getValidInventory(final PactDslWithProvider builder) throws IOException {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        return builder
                .given("Inventory service is running")
                .uponReceiving("GET a valid inventory")
                .method("GET")
                .path("/inventory")
                .willRespondWith()
                .status(200)
                .headers(headers)
                //TODO Refactor the body so that the generated pact allows provider to match on Type and patterns.
                .body("{\"items\":[{\"id\":\"ITEM_ID_1\",\"name\":\"ITEM_NAME_1\",\"quantity\":\"200\",\"backOrder\":\"100\",\"price\":699},{\"id\":\"ITEM_ID_2\",\"name\":\"ITEM_NAME_2\",\"quantity\":\"50\",\"backOrder\":\"0\",\"price\":9999},{\"id\":\"ITEM_ID_3\",\"name\":\"ITEM_NAME_3\",\"quantity\":\"600\",\"backOrder\":\"500\",\"price\":199}]}")
                .toPact();
    }

    @Test
    @PactVerification(value = "inventory", fragment = "getValidInventory")
    public void runTest() {
        RestTemplate call = new RestTemplate();
        Inventory expectedResponse = new Inventory(Arrays.asList(
                new Item("ITEM_ID_1", "ITEM_NAME_1", "200", "100", 699),
                new Item("ITEM_ID_2", "ITEM_NAME_2", "50", "0", 9999),
                new Item("ITEM_ID_3", "ITEM_NAME_3", "600", "500", 199)
        ));

        Inventory actualResponse = call.getForObject(pactProviderRuleMk2.getConfig().url() + "/inventory", Inventory.class);
        //TODO Instead of calling Pact's mock provider, call an actual running end point.

        assertEquals(actualResponse, expectedResponse);
    }
}