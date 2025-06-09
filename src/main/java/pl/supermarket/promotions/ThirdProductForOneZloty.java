package pl.supermarket.promotions;

import pl.supermarket.entity.Product;

import java.util.*;

public class ThirdProductForOneZloty implements Promotion {
    @Override
    public double apply(List<Product> products) {
        if (products.isEmpty()) return 0;

        List<Product> sorted = new ArrayList<>(products);
        sorted.sort(Comparator.comparing(Product::getPrice));
        
        double total = 0;
        
        for (int i = 0; i < sorted.size(); i += 3) {
            int groupEnd = Math.min(i + 3, sorted.size());
            List<Product> group = sorted.subList(i, groupEnd);
            
            if (group.size() >= 3) {
                total += 1.0; 
                total += group.get(1).getPrice(); 
                total += group.get(2).getPrice(); 
            } else {
        
                for (Product p : group) {
                    total += p.getPrice();
                }
            }
        }
        
        return total;
    }


    public String getName(){
        return "ZLOTOWKA";
    }

    @Override
    public String toString() {
        return getName() + ": Przy zakupie 3 produktów najtańszy jest za złotówkę";
    }
}
