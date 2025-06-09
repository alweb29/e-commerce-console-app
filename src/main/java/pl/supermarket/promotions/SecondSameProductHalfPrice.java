package pl.supermarket.promotions;

import pl.supermarket.entity.Product;

import java.util.*;

public class SecondSameProductHalfPrice implements Promotion {
    @Override
    public double apply(List<Product> products) {
        Map<String, List<Product>> grouped = new HashMap<>();
        for (Product p : products) {
            grouped.computeIfAbsent(p.getName(), k -> new ArrayList<>()).add(p);
        }

        double total = 0;
        for (List<Product> group : grouped.values()) {
            int count = group.size();
            for (int i = 0; i < count; i++) {
                if ((i + 1) % 2 == 0) {
                    total += group.get(i).getPrice() * 0.5;
                } else {
                    total += group.get(i).getPrice();
                }
            }
        }
        return total;
    }

    public String getName(){
        return "50PROCENT";
    }

    @Override
    public String toString() {
        return getName() + ": Drugi taki sam produkt połowę ceny";
    }
}
