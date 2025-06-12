package pl.supermarket.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.supermarket.entity.Category;
import pl.supermarket.entity.Product;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pl.supermarket.service.StockService.productNamePriceMap;
import static pl.supermarket.service.StockService.uniqueProducts;

class CatalogServiceTest {

    CatalogService catalogService = new CatalogService();
    List<Product> products = new ArrayList<>();

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

    public Product createProductFromMap(String productName) {
        return new Product(productName, productNamePriceMap.get(productName).getKey(), productNamePriceMap.get(productName).getValue());
    }

    @Test
    void getAllProductsSortedByNameTestWhenAllAvailable() {
        //WHEN:
        List<Product> allProductsSortedByName = catalogService.getAllProductsSortedByName();
        //THEN:
        assertFalse(allProductsSortedByName.isEmpty());
        assertEquals(uniqueProducts.size(), allProductsSortedByName.size());
    }

    @Test
    void getAllProductsSortedByNameTestWhenNoneAvailable() {
        //GIVEN:
        products = new ArrayList<>();
        //WHEN:
        List<Product> allProductsSortedByName = catalogService.getAllProductsSortedByName();
        //THEN:
        assertFalse(allProductsSortedByName.isEmpty());
        assertEquals(uniqueProducts.size(), allProductsSortedByName.size());
    }

    @Test
    void getAvailableProductsByCategorySortedByPriceTestWhenOwoceAvailable() {
        //WHEN:
        List<Product> owoceList = catalogService.getAvailableProductsByCategorySortedByPrice(Category.OWOCE);
        //THEN:
        assertFalse(owoceList.isEmpty());
        assertEquals(3, owoceList.size());
        assertEquals(2.5, owoceList.get(0).getPrice());
        assertEquals(3, owoceList.get(1).getPrice());
        assertEquals(3, owoceList.get(2).getPrice());
    }

    @Test
    void getAvailableProductsByCategorySortedByPriceTestWhenInneNotAvailable() {
        //WHEN:
        List<Product> itemsList = catalogService.getAvailableProductsByCategorySortedByPrice(Category.INNE);
        //THEN:
        assertTrue(itemsList.isEmpty());
    }

    @Test
    void getSortFilterDescriptionTestWhenSortingByCategory() {
        //WHEN:
        catalogService.setSortingByCategory(true);
        catalogService.setSortingCategory(Category.OWOCE);
        String sortFilterDescription = catalogService.getSortFilterDescription();
        //THEN:
        assertEquals("Według kategorii -> OWOCE", sortFilterDescription);
    }

    @Test
    void getSortFilterDescriptionTestWhenSortingAlphabetically() {
        //WHEN:
        catalogService.setSortingByCategory(false);
        String sortFilterDescription = catalogService.getSortFilterDescription();
        //THEN:
        assertEquals("Alfabetyczne", sortFilterDescription);
    }
}