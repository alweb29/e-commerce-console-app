package pl.supermarket.service;

import lombok.Getter;
import pl.supermarket.entity.Product;
import pl.supermarket.promotions.Promotion;

import java.util.*;
import java.util.stream.Collectors;

import static pl.supermarket.service.StockService.createProductFromMap;


public class ShoppingCartService {
    @Getter
    private final List<Product> products = new ArrayList<>();
    private Promotion promotion = null;

    public void setPromotion(Promotion promotion) {
        System.out.println("Zastosowano promocję: " + promotion);
        this.promotion = promotion;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Optional<Product> removeProduct(String productName) {
        boolean removed =  products.removeIf(product -> product.getName().equals(productName));
        return removed ? Optional.of(createProductFromMap(productName)) : Optional.empty();
    }

    public boolean removeProductByName(String name) {
        Optional<Product> toRemove = products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst();
        toRemove.ifPresent(products::remove);
        return toRemove.isPresent();
    }

    public void displayCart() {
        System.out.println("===KOSZYK===");
        if (products.isEmpty()) {
            System.out.println("\nKoszyk jest pusty.");
            return;
        }

        printProducts();
        printPromotion();
        System.out.printf("\nAktualna cena: %.2f%n", calculateTotalPrice());
    }

    private void printProducts() {
        Map<String, Long> grouped = products.stream()
                .collect(Collectors.groupingBy(Product::getName, LinkedHashMap::new, Collectors.counting()));

        System.out.println("# Zawartość koszyka:");
        for (Map.Entry<String, Long> entry : grouped.entrySet()) {
            System.out.printf("%s, %d szt.\n", entry.getKey(), entry.getValue());
        }
    }

    private void printPromotion() {
        if (promotion != null) {
            System.out.println("\nZastosowana promocja: " + promotion);
        } else {
            System.out.println("\nNie zastosowano żadnej promocji");
        }
    }

    public void buyProducts() {
        System.out.println("\nZakupiono towary za: " + calculateTotalPrice() + "zł");
        products.clear();
    }

    public double calculateTotalPrice() {
        if (promotion != null) {
            return promotion.apply(products);
        } else {
            return products.stream().mapToDouble(Product::getPrice).sum();
        }
    }
}
