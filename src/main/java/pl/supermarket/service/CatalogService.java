package pl.supermarket.service;

import lombok.Getter;
import lombok.Setter;
import pl.supermarket.entity.Category;
import pl.supermarket.entity.Product;

import java.util.*;
import java.util.stream.Collectors;

import static pl.supermarket.service.StockService.uniqueProducts;

public class CatalogService {
    private final List<Product> availableProducts;
    @Getter
    @Setter
    private boolean isSortingByCategory;

    @Setter
    private Category sortingCategory;

    public CatalogService() {
        availableProducts = new ArrayList<>(StockService.products);
    }

    public void printCatalogContent() {
        System.out.println("===KATALOG===");
        System.out.println("Sortowanie: " + getSortFilterDescription());
        System.out.println("# Produkty:");

        List<Product> productsForDisplay;
        if (isSortingByCategory) {
            productsForDisplay = getAvailableProductsByCategorySortedByPrice(sortingCategory);
        } else {
            productsForDisplay = getAllProductsSortedByName();
        }

        Map<String, List<Product>> grouped = productsForDisplay.stream()
                .collect(Collectors.groupingBy(p ->
                        p.getName() + "|" + p.getPrice() + "|" + p.getCategory()));

        List<ProductSummary> summaries = grouped.keySet().stream()
                .map(productList -> {
                    String[] parts = productList.split("\\|");
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    Category category = Category.valueOf(parts[2]);
                    long quantity = availableProducts.stream().filter(product -> name.equals(product.getName())).count();
                    return new ProductSummary(name, price, category, quantity);
                })
                .sorted(getCurrentComparator())
                .toList();

        int index = 1;
        for (ProductSummary summary : summaries) {
            System.out.printf("%d. %s - %.2f zł (%s) [x%d]\n",
                    index++, summary.name, summary.price, summary.category, summary.quantity);
        }

        if (summaries.isEmpty()) {
            System.out.println("\nBrak produktów w wybranej kategorii");
        }
    }

    public List<Product> getAllProductsSortedByName() {
        return uniqueProducts.stream()
                .sorted(Comparator.comparing(Product::getName))
                .toList();
    }

    public List<Product> getAvailableProductsByCategorySortedByPrice(Category category) {
        return availableProducts.stream()
                .filter(p -> p.getCategory() == category)
                .sorted(Comparator.comparing(Product::getPrice))
                .toList();
    }

    public String getSortFilterDescription() {
        return isSortingByCategory ?
                "Według kategorii -> " + sortingCategory.toString() :
                "Alfabetyczne";
    }

    private Comparator<ProductSummary> getCurrentComparator() {
        if (isSortingByCategory) {
            return Comparator.comparingDouble(p -> p.price);
        } else {
            return Comparator.comparing(p -> p.name);
        }
    }

    public void removeItems(List<Product> productsToRemove) {
        for (Product product : productsToRemove) {
            availableProducts.remove(product);
        }
    }

    public Optional<Product> printAndChooseProductToAdd(Scanner scanner) {
        Map<String, List<Product>> grouped = availableProducts.stream()
                .collect(Collectors.groupingBy(p -> p.getName() + "|" + p.getPrice() + "|" + p.getCategory()));

        List<Map.Entry<ProductSummary, List<Product>>> summarizedEntries = grouped.entrySet().stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split("\\|");
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    Category category = Category.valueOf(parts[2]);
                    int quantity = entry.getValue().size();
                    ProductSummary summary = new ProductSummary(name, price, category, quantity);
                    return Map.entry(summary, entry.getValue());
                })
                .sorted(Map.Entry.comparingByKey(Comparator.comparing((ProductSummary ps) -> ps.name)))
                .toList();

        System.out.println();
        for (int i = 0; i < summarizedEntries.size(); i++) {
            ProductSummary summary = summarizedEntries.get(i).getKey();
            System.out.printf("%d. %s - %.2f zł (%s) [x%d]\n", i + 1, summary.name, summary.price, summary.category, summary.quantity);
        }

        System.out.print("\nPodaj numer produktu do dodania: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < summarizedEntries.size()) {
                Product chosen = summarizedEntries.get(index).getValue().getFirst();
                System.out.println("Dodano do koszyka: " + chosen);
                availableProducts.remove(chosen);
                return Optional.of(chosen);
            } else {
                System.out.println("Nieprawidłowy wybór.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Niepoprawny numer.");
        }
        return Optional.empty();
    }

    public void addProductToAvailableProducts(Product product) {
        availableProducts.add(product);
    }

    private static class ProductSummary {
        String name;
        double price;
        Category category;
        long quantity;

        ProductSummary(String name, double price, Category category, long quantity) {
            this.name = name;
            this.price = price;
            this.category = category;
            this.quantity = quantity;
        }
    }


}
