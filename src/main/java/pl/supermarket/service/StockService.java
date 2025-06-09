package pl.supermarket.service;

import pl.supermarket.entity.Category;
import pl.supermarket.entity.Product;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StockService {
    public static Map<String, Map.Entry<Double, Category>> productNamePriceMap = Map.ofEntries(
            Map.entry("Jabłko", Map.entry(2.50, Category.OWOCE)),
            Map.entry("Banan", Map.entry(3.00, Category.OWOCE)),
            Map.entry("Mleko", Map.entry(4.00, Category.NABIAL)),
            Map.entry("Chleb", Map.entry(3.50, Category.PIECZYWO)),
            Map.entry("Sok pomarańczowy", Map.entry(5.50, Category.NAPOJE))
    );
    public static List<Product> products = List.of(
            createProductFromMap("Jabłko"),
            createProductFromMap("Banan"),
            createProductFromMap("Banan"),
            createProductFromMap("Mleko"),
            createProductFromMap("Chleb"),
            createProductFromMap("Sok pomarańczowy")
    );

    public static Set<Product> uniqueProducts = new HashSet<>();
    static {
        productNamePriceMap.keySet().forEach(key -> uniqueProducts.add(createProductFromMap(key)));
    }

    public static Product createProductFromMap(String productName) {
        return new Product(productName, productNamePriceMap.get(productName).getKey(), productNamePriceMap.get(productName).getValue());
    }
}
