package pl.supermarket;

import java.util.List;

public class TenPercentDiscount implements Promotion {
    @Override
    public double apply(List<Product> products) {
        double sum = products.stream().mapToDouble(Product::getPrice).sum();
        return sum * 0.9;
    }
}
