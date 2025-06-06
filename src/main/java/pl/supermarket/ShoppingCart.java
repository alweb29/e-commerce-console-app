package pl.supermarket;

import java.util.*;
import java.util.stream.Collectors;


public class ShoppingCart {
    private final List<Product> products = new ArrayList<>();
    private Promotion promotion = null;

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean removeProductByName(String name) {
        Optional<Product> toRemove = products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst();
        toRemove.ifPresent(products::remove);
        return toRemove.isPresent();
    }

    public void displayCart() {
        if (products.isEmpty()) {
            System.out.println("Koszyk jest pusty.");
            return;
        }

        Map<String, Long> grouped = products.stream()
                .collect(Collectors.groupingBy(Product::getName, LinkedHashMap::new, Collectors.counting()));

        System.out.println("Zawartość koszyka:");
        for (Map.Entry<String, Long> entry : grouped.entrySet()) {
            System.out.printf("%s, %d szt.\n", entry.getKey(), entry.getValue());
        }
    }

    public void setPromotion(Promotion promo) {
        this.promotion = promo;
    }

    public double calculateTotalPrice() {
        if (promotion != null) {
            return promotion.apply(products);
        } else {
            return products.stream().mapToDouble(Product::getPrice).sum();
        }
    }
}
