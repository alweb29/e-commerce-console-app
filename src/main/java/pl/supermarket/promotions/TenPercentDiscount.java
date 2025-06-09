package pl.supermarket.promotions;

import pl.supermarket.entity.Product;

import java.util.List;

public class TenPercentDiscount implements Promotion {
    @Override
    public double apply(List<Product> products) {
        double sum = products.stream().mapToDouble(Product::getPrice).sum();
        return sum * 0.9;
    }

    public String getName(){
        return "PROMO10";
    }

    @Override
    public String toString() {
        return getName() + ": 10% taniej na wszystkie produkty w koszyku";
    }
}
