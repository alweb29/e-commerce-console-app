package pl.supermarket;

public class Product {
    private final String name;
    private final double price;
    private final Category category;
    private final boolean available;

    public Product(String name, double price, Category category, boolean available) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }
}
