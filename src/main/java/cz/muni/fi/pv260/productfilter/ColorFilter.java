package cz.muni.fi.pv260.productfilter;

/**
 * Passes if the color of the product is exactly the color this filter checks for.
 */
public class ColorFilter implements Filter<Product> {

    private Color color;

    public ColorFilter(Color color) {
        this.color = color;
    }

    @Override
    public boolean passes(Product item) {
        return color.equals(item.getColor());
    }

}
