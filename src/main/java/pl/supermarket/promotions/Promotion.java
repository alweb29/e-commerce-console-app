package pl.supermarket.promotions;

import pl.supermarket.entity.Product;

import java.util.List;

public interface Promotion {
    String getName();

    double apply(List<Product> products);
}
