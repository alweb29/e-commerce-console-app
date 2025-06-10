package pl.supermarket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.supermarket.entity.Product;
import pl.supermarket.promotions.SecondSameProductHalfPrice;
import pl.supermarket.promotions.TenPercentDiscount;
import pl.supermarket.promotions.ThirdProductForOneZloty;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pl.supermarket.service.StockService.*;

class ShoppingCartServiceTest {

    List<Product> products = new ArrayList<>();
    ShoppingCartService shoppingCartService = new ShoppingCartService();

    @BeforeEach
    void setUp() {
        //GIVEN:
        products = List.of(
                createProductFromMap("Jabłko"),
                createProductFromMap("Banan"),
                createProductFromMap("Banan"),
                createProductFromMap("Mleko"),
                createProductFromMap("Chleb"),
                createProductFromMap("Sok pomarańczowy")
        );

        uniqueProducts.clear();
        productNamePriceMap.keySet().forEach(key -> uniqueProducts.add(createProductFromMap(key)));
    }

    @Test
    void removeProductByNameTestWhenProductExists() {
        //GIVEN:
        shoppingCartService.addProduct(products.getFirst());
        //WHEN:
        boolean removed = shoppingCartService.removeProductByName(products.getFirst().getName());
        //THEN:
        assertTrue(removed);
    }

    @Test
    void removeProductByNameTestWhenProductNotExists() {
        //WHEN:
        boolean removed = shoppingCartService.removeProductByName(products.getFirst().getName());
        //THEN:
        assertFalse(removed);
    }

    @Test
    void calculateTotalPriceTestWhenNoProductExists() {
        //WHEN:
        double price = shoppingCartService.calculateTotalPrice();
        //THEN:
        assertEquals(0, price);
    }

    @Test
    void calculateTotalPriceTestWhenNoPromotion() {
        //GIVEN:
        shoppingCartService.addProduct(products.getFirst());
        shoppingCartService.addProduct(products.get(1));
        //WHEN:
        double price = shoppingCartService.calculateTotalPrice();
        //THEN:
        assertEquals(5.5, price);
    }

    @Test
    void calculateTotalPriceTestWhen10PercentPromotion() {
        //GIVEN:
        shoppingCartService.addProduct(createProductFromMap("Banan"));
        shoppingCartService.addProduct(createProductFromMap("Mleko"));
        shoppingCartService.addProduct(createProductFromMap("Mleko"));
        shoppingCartService.addProduct(createProductFromMap("Mleko"));

        shoppingCartService.setPromotion(new TenPercentDiscount());
        //WHEN:
        double price = shoppingCartService.calculateTotalPrice();
        //THEN:
        assertEquals(13.5, price);
    }

    @Test
    void calculateTotalPriceTestWhenSecondSameProductHalfPricePromotion() {
        //GIVEN:
        shoppingCartService.addProduct(createProductFromMap("Mleko"));
        shoppingCartService.addProduct(createProductFromMap("Mleko"));

        shoppingCartService.setPromotion(new SecondSameProductHalfPrice());
        //WHEN:
        double price = shoppingCartService.calculateTotalPrice();
        //THEN:
        assertEquals(6, price);
    }

    @Test
    void calculateTotalPriceTestWhenThirdProductOneZlotyPromotion() {
        //GIVEN:
        shoppingCartService.addProduct(createProductFromMap("Mleko"));
        shoppingCartService.addProduct(createProductFromMap("Mleko"));
        shoppingCartService.addProduct(createProductFromMap("Mleko"));

        shoppingCartService.setPromotion(new ThirdProductForOneZloty());
        //WHEN:
        double price = shoppingCartService.calculateTotalPrice();
        //THEN:
        assertEquals(9, price);
    }
}