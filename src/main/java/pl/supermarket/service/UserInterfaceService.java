package pl.supermarket.service;

import lombok.RequiredArgsConstructor;
import pl.supermarket.entity.Category;
import pl.supermarket.entity.Product;
import pl.supermarket.promotions.Promotion;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;


@RequiredArgsConstructor
public class UserInterfaceService {

    private final CatalogService catalog;
    private final ShoppingCartService cart;
    private final Scanner scanner = new Scanner(System.in);
    private final List<Promotion> promotions;
    private ShopViewScreen screenState = ShopViewScreen.MENU;
    private boolean running = true;

    private void printContent(ShopViewScreen screenState) {
        switch (screenState) {
            case MENU:
                addSpacing();
                System.out.println("\n=== MENU ===");
                System.out.println("1. Wyświetl katalog produktów");
                System.out.println("2. Wyświetl koszyk");
                System.out.println("0. Wyjdź");
                break;
            case CART:
                addSpacing();
                cart.displayCart();
                System.out.println("\n# Dostepne opcje:");
                System.out.println("1. Dodaj produkt");
                System.out.println("2. Usuń produkt");
                System.out.println("3. Kup produkty");
                System.out.println("4. Dodaj kod promocyjny");
                System.out.println("5. Przejdź do katalogu");
                System.out.println("6. Przejdź do menu");
                break;
            case CATALOG:
                addSpacing();
                catalog.printCatalogContent();

                System.out.println("\n# Dostepne opcje:");
                System.out.println("1. Dodaj produkt");
                System.out.println("2. Zmień sortowanie");
                System.out.println("3. Przejdź do koszyka");
                System.out.println("4. Przejdź do menu");
                break;
            default:
                addSpacing();
                System.out.println("\n\n Do widzenia!");
        }
    }

    private void addSpacing() {
        System.out.println("\n\n\n\n");
    }

    public void run() {
        while (running) {
            printContent(screenState);

            System.out.print("Wybierz opcję: ");
            String choice = scanner.nextLine().trim();

            handleChoice(choice, screenState);
        }
        scanner.close();
    }

    private void handleChoice(String choice, ShopViewScreen screenState) {
        switch (screenState) {
            case MENU:
                switch (choice) {
                    case "1":
                        setScreenState(ShopViewScreen.CATALOG);
                        break;
                    case "2":
                        setScreenState(ShopViewScreen.CART);
                        break;
                    case "0":
                        System.out.println("Do widzenia!");
                        running = false;
                        break;
                    default:
                        System.out.println("Nieprawidłowa opcja.");
                }
                break;
            case CATALOG:
                switch (choice) {
                    case "1":
                        addProduct();
                        break;
                    case "2":
                        filterCatalog();
                        break;
                    case "3":
                        setScreenState(ShopViewScreen.CART);
                        break;
                    case "4":
                        setScreenState(ShopViewScreen.MENU);
                        break;
                    default:
                        System.out.println("Nieprawidłowa opcja.");
                }
                break;
            case CART:
                switch (choice) {
                    case "1":
                        addProduct();
                        break;
                    case "2":
                        deleteProductFromCart();
                        break;
                    case "3":
                        buyItems();
                        break;
                    case "4":
                        applyPromotion();
                        break;
                    case "5":
                        setScreenState(ShopViewScreen.CATALOG);
                        break;
                    case "6":
                        setScreenState(ShopViewScreen.MENU);
                        break;
                    default:
                        System.out.println("Nieprawidłowa opcja.");
                }
                break;
        }
    }

    private void buyItems() {
        if (!cart.getProducts().isEmpty()) {
            this.catalog.removeItems(cart.getProducts());
            cart.buyProducts();
        } else {
            System.out.println("\nNie masz produktów w koszyku");
        }
    }

    private void filterCatalog() {
        for (Category category: Category.values()) {
            System.out.println("# " + category);
        }
        System.out.println("# ALFABETYCZNIE");
        System.out.print("Wybierz sposób sortowania: ");
        String category = scanner.nextLine().trim();

        try {
            Category categoryEnum = Category.valueOf(category.toUpperCase());
            catalog.setSortingCategory(categoryEnum);
            catalog.setSortingByCategory(true);
        } catch (IllegalArgumentException e) {
            catalog.setSortingByCategory(false);
        }
    }

    private void applyPromotion() {
        System.out.print("Podaj kod promocyjny: ");
        String code = scanner.nextLine().trim();
        Optional<Promotion> matchingPromotionFound = promotions.stream()
                .filter(promotion -> code.equalsIgnoreCase(promotion.getName()))
                .findFirst();
        if (matchingPromotionFound.isPresent()) {
            cart.setPromotion(matchingPromotionFound.get());
        } else {
            System.out.println("Niepoprawny kod.");
        }
    }

    private void addProduct() {
        catalog.printAndChooseProductToAdd(scanner)
                .ifPresent(cart::addProduct);
    }

    private void deleteProductFromCart() {
        System.out.print("Podaj nazwę produktu do usunięcia: ");
        String nameToRemove = scanner.nextLine();
        Optional<Product> product = cart.removeProduct(nameToRemove);
        if (product.isPresent()) {
            catalog.addProductToAvailableProducts(product.get());
            System.out.println("Usunięto produkt.");
        } else {
            System.out.println("Nie znaleziono produktu.");
        }
    }

    private void setScreenState(ShopViewScreen screenState) {
        this.screenState = screenState;
    }

    enum ShopViewScreen {
        MENU,
        CATALOG,
        CART
    }
}
