package pl.supermarket;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ThirdProductForOneZloty implements Promotion {
    @Override
    public double apply(List<Product> products) {
        List<Product> sorted = products.stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());

        double total = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if ((i + 1) % 3 == 0) {
                total += 1.0;
            } else {
                total += sorted.get(i).getPrice();
            }
        }
        return total;
    }
}
