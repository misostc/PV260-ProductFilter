package cz.muni.fi.pv260.productfilter;

import java.util.Collection;

/**
 * Can represent some form into which the selected products are displayed,
 * serialized representation of the collection etc.
 */
public interface Output {

    void postSelectedProducts(Collection<Product> products);

}
