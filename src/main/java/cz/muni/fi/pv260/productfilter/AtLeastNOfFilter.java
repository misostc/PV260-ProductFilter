package cz.muni.fi.pv260.productfilter;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;

/**
 * This filter passes if N or more of child filters pass (it does not matter how many fail)
 */
public class AtLeastNOfFilter<T> implements Filter<T> {

    private int n;
    private List<Filter<T>> filters;

    /**
     * @param n       how many child filters have to pass for this to pass
     * @param filters child filters
     * @throws FilterNeverSucceeds      if n is higher than number of child filters
     * @throws IllegalArgumentException if n is 0 or lower (the filter would always pass)
     */
    public AtLeastNOfFilter(int n, Filter<T>... filters) {
        this.n = n;
        this.filters = new ArrayList<>(asList(filters));
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        if (n > this.filters.size()) {
            throw new FilterNeverSucceeds();
        }
    }

    @Override
    public boolean passes(T item) {
        int passed = 0;
        for (Filter<T> f : filters) {
            if (f.passes(item)) {
                passed++;
            }
        }
        return passed >= n;
    }

}
