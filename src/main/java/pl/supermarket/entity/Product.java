package pl.supermarket.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Product {
    private final String name;
    private final double price;
    private final Category category;

    @Override
    public String toString() {
        return String.format("Produkt: %s, z kategorii: %s, o cenie %.2f", name, category, price);
    }
}
