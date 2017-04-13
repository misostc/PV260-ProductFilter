package cz.muni.fi.pv260.productfilter;

import java.util.Collection;

/**
 * Can encapsulate any storage of available products.
 * Implementations can be anything from simple Collections to disk-based databases.
 */
public interface Input {

    Collection<Product> obtainProducts() throws ObtainFailedException;

}
