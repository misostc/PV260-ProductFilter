package cz.muni.fi.pv260.productfilter;

/**
 * Can be used to filter some collection of items.
 * It is up to client to iterate over the items.
 */
public interface Filter<T> {

    /**
     * @param item decide whether this item passes criteria of the filter
     * @return true if the item passes
     */
    boolean passes(T item);

}
