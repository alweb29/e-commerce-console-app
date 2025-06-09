package pl.supermarket;

import pl.supermarket.promotions.SecondSameProductHalfPrice;
import pl.supermarket.promotions.TenPercentDiscount;
import pl.supermarket.promotions.ThirdProductForOneZloty;
import pl.supermarket.service.CatalogService;
import pl.supermarket.service.ShoppingCartService;
import pl.supermarket.service.UserInterfaceService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        new UserInterfaceService(
                new CatalogService(),
                new ShoppingCartService(),
                List.of(new SecondSameProductHalfPrice(),
                        new TenPercentDiscount(),
                        new ThirdProductForOneZloty())
                ).run();
    }
}
