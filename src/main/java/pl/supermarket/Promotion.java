package pl.supermarket;

import java.util.List;

public interface Promotion {
    double apply(List<Product> products);
}
