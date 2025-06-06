package pl.supermarket;

import java.util.*;

public class ProductCatalog {
    private final List<Product> products = new ArrayList<>();

    public ProductCatalog() {
        // Predefiniowane produkty
        products.add(new Product("Jabłko", 2.50, Category.OWOCE, true));
        products.add(new Product("Banan", 3.00, Category.OWOCE, true));
        products.add(new Product("Mleko", 4.00, Category.NABIAL, true));
        products.add(new Product("Chleb", 3.50, Category.PIECZYWO, false));
        products.add(new Product("Sok pomarańczowy", 5.50, Category.NAPOJE, true));
    }

    public List<Product> getAllProductsSortedByName() {
        return products.stream()
                .sorted(Comparator.comparing(Product::getName))
                .toList();
    }

    public List<Product> getAvailableProductsByCategorySortedByPrice(Category category) {
        return products.stream()
                .filter(p -> p.getCategory() == category && p.isAvailable())
                .sorted(Comparator.comparing(Product::getPrice))
                .toList();
    }
}
