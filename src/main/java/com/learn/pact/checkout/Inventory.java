package com.learn.pact.checkout;

import java.util.List;
import java.util.Objects;

public class Inventory {
    private List<Item> items;

    public Inventory(List<Item> items) {
        this.items = items;
    }

    public Inventory() {}

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return this.items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(getItems(), inventory.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItems());
    }
}
