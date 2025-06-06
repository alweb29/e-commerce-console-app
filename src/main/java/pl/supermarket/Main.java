package pl.supermarket;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductCatalog catalog = new ProductCatalog();
        ShoppingCart cart = new ShoppingCart();

        boolean running = true;

        while (running) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Wyświetl produkty");
            System.out.println("2. Dodaj produkt do koszyka");
            System.out.println("3. Usuń produkt z koszyka");
            System.out.println("4. Wyświetl koszyk");
            System.out.println("5. Wpisz kod promocyjny");
            System.out.println("6. Pokaż cenę końcową");
            System.out.println("0. Wyjdź");
            System.out.print("Wybierz opcję: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    List<Product> products = catalog.getAllProductsSortedByName();
                    System.out.println("Produkty:");
                    for (int i = 0; i < products.size(); i++) {
                        Product p = products.get(i);
                        System.out.printf("%d. %s - %.2f zł (%s)\n", i + 1, p.getName(), p.getPrice(), p.getCategory());
                    }
                    break;

                case "2":
                    List<Product> all = catalog.getAllProductsSortedByName();
                    System.out.print("Podaj numer produktu do dodania: ");
                    try {
                        int index = Integer.parseInt(scanner.nextLine()) - 1;
                        if (index >= 0 && index < all.size() && all.get(index).isAvailable()) {
                            cart.addProduct(all.get(index));
                            System.out.println("Dodano do koszyka.");
                        } else {
                            System.out.println("Nieprawidłowy wybór lub produkt niedostępny.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Niepoprawny numer.");
                    }
                    break;

                case "3":
                    System.out.print("Podaj nazwę produktu do usunięcia: ");
                    String nameToRemove = scanner.nextLine();
                    boolean removed = cart.removeProductByName(nameToRemove);
                    if (removed) {
                        System.out.println("Usunięto produkt.");
                    } else {
                        System.out.println("Nie znaleziono produktu.");
                    }
                    break;

                case "4":
                    cart.displayCart();
                    break;

                case "5":
                    System.out.print("Podaj kod promocyjny: ");
                    String code = scanner.nextLine().trim();
                    switch (code) {
                        case "PROMO10":
                            cart.setPromotion(new TenPercentDiscount());
                            System.out.println("Zastosowano promocję 10%.");
                            break;
                        case "ZLOTOWKA":
                            cart.setPromotion(new ThirdProductForOneZloty());
                            System.out.println("Zastosowano promocję: najtańszy co 3 za 1 zł.");
                            break;
                        case "50PROCENT":
                            cart.setPromotion(new SecondSameProductHalfPrice());
                            System.out.println("Zastosowano promocję: drugi taki sam za 50%.");
                            break;
                        default:
                            System.out.println("Niepoprawny kod.");
                    }
                    break;

                case "6":
                    System.out.printf("Cena końcowa koszyka: %.2f zł\n", cart.calculateTotalPrice());
                    break;

                case "0":
                    running = false;
                    System.out.println("Do widzenia!");
                    break;

                default:
                    System.out.println("Nieprawidłowa opcja.");
            }
        }

        scanner.close();
    }
}
