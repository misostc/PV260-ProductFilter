package cz.muni.fi.pv260.productfilter;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents type of item offered, not an individual item.
 */
public class Product {

    private final int id;
    private final String name;
    private final Color color;
    private final BigDecimal price;

    public Product(int id, String name, Color color, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Product)) {
            return false;
        }
        Product other = (Product) obj;
        return id == other.id && name.equals(other.name) && color.equals(other.color) && price.equals(other.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, price);
    }
}
