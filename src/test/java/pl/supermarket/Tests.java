package pl.supermarket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class SupermarketTest {

    private ShoppingCart cart;
    private ProductCatalog catalog;
    private Product apple;
    private Product banana;
    private Product milk;
    
    @BeforeEach
    void setUp() {
        cart = new ShoppingCart();
        catalog = new ProductCatalog();
        
        apple = new Product("Jabłko", 2.50, Category.OWOCE, true);
        banana = new Product("Banan", 3.00, Category.OWOCE, true);
        milk = new Product("Mleko", 4.00, Category.NABIAL, true);
    }

    // ===== TESTY KOSZYKA ZAKUPÓW =====
    
    @Test
    @DisplayName("Powinien obliczyć cenę koszyka bez promocji")
    void shouldCalculateTotalPriceWithoutPromotion() {
        // Given - mamy pusty koszyk i produkty do dodania (produkty już przygotowane w setUp)
        // When - dodajemy produkty do koszyka i obliczamy cenę
        cart.addProduct(apple);
        cart.addProduct(banana);
        cart.addProduct(milk);
        double totalPrice = cart.calculateTotalPrice();
        
        // Then - sprawdzamy czy cena jest poprawna
        assertEquals(9.50, totalPrice, 0.01, 
            "Cena koszyka powinna wynosić 2.50 + 3.00 + 4.00 = 9.50 zł");
    }
    
    @Test
    @DisplayName("Powinien usunąć produkt z koszyka po nazwie")
    void shouldRemoveProductByName() {
        // Given - mamy koszyk z produktami
        cart.addProduct(apple);
        cart.addProduct(banana);
        double initialPrice = cart.calculateTotalPrice();
        
        // When - usuwamy produkt po nazwie
        boolean wasRemoved = cart.removeProductByName("Jabłko");
        double finalPrice = cart.calculateTotalPrice();
        
        // Then - sprawdzamy czy produkt został usunięty
        assertTrue(wasRemoved, "Produkt powinien zostać usunięty");
        assertEquals(5.50, initialPrice, 0.01, "Początkowa cena powinna wynosić 5.50 zł");
        assertEquals(3.00, finalPrice, 0.01, "Końcowa cena powinna wynosić 3.00 zł");
    }
    
    @Test
    @DisplayName("Powinien zwrócić false przy próbie usunięcia nieistniejącego produktu")
    void shouldReturnFalseWhenRemovingNonExistentProduct() {
        // Given - mamy koszyk z jednym produktem
        cart.addProduct(apple);
        
        // When - próbujemy usunąć nieistniejący produkt
        boolean wasRemoved = cart.removeProductByName("Nieistniejący Produkt");
        
        // Then - operacja powinna zwrócić false
        assertFalse(wasRemoved, "Usuwanie nieistniejącego produktu powinno zwrócić false");
        assertEquals(2.50, cart.calculateTotalPrice(), 0.01, 
            "Cena koszyka nie powinna się zmienić");
    }

    // ===== TESTY PROMOCJI =====
    
    @Test
    @DisplayName("Powinien zastosować promocję 10% zniżki")
    void shouldApplyTenPercentDiscount() {
        // Given - mamy koszyk z produktami i promocję 10%
        cart.addProduct(apple);  // 2.50
        cart.addProduct(banana); // 3.00
        cart.addProduct(milk);   // 4.00
        // Suma: 9.50 zł
        TenPercentDiscount promotion = new TenPercentDiscount();
        
        // When - aplikujemy promocję
        cart.setPromotion(promotion);
        double discountedPrice = cart.calculateTotalPrice();
        
        // Then - cena powinna być pomniejszona o 10%
        assertEquals(8.55, discountedPrice, 0.01, 
            "Cena z promocją 10% powinna wynosić 9.50 * 0.9 = 8.55 zł");
    }
    
    @Test
    @DisplayName("Powinien zastosować promocję 'drugi taki sam za połowę ceny'")
    void shouldApplySecondSameProductHalfPrice() {
        // Given - mamy 3 takie same produkty w koszyku
        cart.addProduct(apple);  // 2.50 - pełna cena
        cart.addProduct(apple);  // 2.50 - 50% = 1.25
        cart.addProduct(apple);  // 2.50 - pełna cena
        cart.addProduct(banana); // 3.00 - pełna cena (tylko jeden)
        SecondSameProductHalfPrice promotion = new SecondSameProductHalfPrice();
        
        // When - aplikujemy promocję
        cart.setPromotion(promotion);
        double discountedPrice = cart.calculateTotalPrice();
        
        // Then - co drugi taki sam produkt za połowę ceny
        double expectedPrice = 2.50 + 1.25 + 2.50 + 3.00; // = 9.25
        assertEquals(expectedPrice, discountedPrice, 0.01,
            "Cena powinna wynosić: 2.50 + 1.25 + 2.50 + 3.00 = 9.25 zł");
    }
    
    @Test
    @DisplayName("Powinien zastosować promocję 'najtańszy co 3 za złotówkę'")
    void shouldApplyThirdProductForOneZloty() {
        // Given - mamy różne produkty w koszyku
        Product cheapProduct = new Product("Cukierek", 1.00, Category.INNE, true);
        Product expensiveProduct = new Product("Czekolada", 5.00, Category.INNE, true);
        
        cart.addProduct(expensiveProduct); // 5.00
        cart.addProduct(apple);           // 2.50  
        cart.addProduct(cheapProduct);    // 1.00 - najtańszy, będzie za 1 zł
        cart.addProduct(banana);          // 3.00 - zostaje poza promocją
        
        ThirdProductForOneZloty promotion = new ThirdProductForOneZloty();
        
        // When - aplikujemy promocję
        cart.setPromotion(promotion);
        double discountedPrice = cart.calculateTotalPrice();
        
        // Then - najtańszy z pierwszych 3 za 1 zł
        // Po sortowaniu: [1.00, 2.50, 5.00] + [3.00]
        // Pierwsza grupa: 1zł + 2.50 + 5.00 = 8.50
        // Druga grupa: 3.00
        // Razem: 11.50
        assertEquals(11.50, discountedPrice, 0.01,
            "Cena powinna wynosić: 1.00(→1zł) + 2.50 + 5.00 + 3.00 = 11.50 zł");
    }

    // ===== TESTY KATALOGU PRODUKTÓW =====
    
    @Test
    @DisplayName("Powinien zwrócić produkty posortowane alfabetycznie")
    void shouldReturnProductsSortedAlphabetically() {
        // Given - mamy katalog z produktami (już utworzony w setUp)
        
        // When - pobieramy wszystkie produkty posortowane
        List<Product> sortedProducts = catalog.getAllProductsSortedByName();
        
        // Then - produkty powinny być posortowane alfabetycznie
        assertFalse(sortedProducts.isEmpty(), "Katalog nie powinien być pusty");
        
        // Sprawdzamy czy pierwszy produkt alfabetycznie to "Banan"
        assertEquals("Banan", sortedProducts.get(0).getName(), 
            "Pierwszy produkt alfabetycznie powinien to być Banan");
        
        // Sprawdzamy czy produkty są rzeczywiście posortowane
        for (int i = 1; i < sortedProducts.size(); i++) {
            String current = sortedProducts.get(i).getName();
            String previous = sortedProducts.get(i-1).getName();
            assertTrue(current.compareTo(previous) >= 0, 
                "Produkty powinny być posortowane alfabetycznie");
        }
    }
    
    @Test
    @DisplayName("Powinien zwrócić tylko dostępne produkty z danej kategorii posortowane po cenie")
    void shouldReturnAvailableProductsByCategorySortedByPrice() {
        // Given - mamy katalog z produktami (zawiera produkty z kategorii OWOCE)
        
        // When - pobieramy dostępne produkty z kategorii OWOCE
        List<Product> fruitsProducts = catalog.getAvailableProductsByCategorySortedByPrice(Category.OWOCE);
        
        // Then - powinniśmy dostać tylko dostępne owoce posortowane po cenie
        assertFalse(fruitsProducts.isEmpty(), "Powinny być dostępne owoce w katalogu");
        
        // Wszystkie produkty powinny być z kategorii OWOCE i dostępne
        for (Product product : fruitsProducts) {
            assertEquals(Category.OWOCE, product.getCategory(), 
                "Wszystkie produkty powinny być z kategorii OWOCE");
            assertTrue(product.isAvailable(), 
                "Wszystkie produkty powinny być dostępne");
        }
        
        // Sprawdzamy sortowanie po cenie (rosnąco)
        for (int i = 1; i < fruitsProducts.size(); i++) {
            double currentPrice = fruitsProducts.get(i).getPrice();
            double previousPrice = fruitsProducts.get(i-1).getPrice();
            assertTrue(currentPrice >= previousPrice, 
                "Produkty powinny być posortowane po cenie rosnąco");
        }
    }
}