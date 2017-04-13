package cz.muni.fi.pv260.productfilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Controller {

    public static final String TAG_CONTROLLER = Controller.class.getSimpleName();
    private Input in;
    private Output out;
    private Logger log;

    /**
     * @throws IllegalArgumentException if any of the arguments are null
     */
    public Controller(Input in, Output out, Logger log) {
        if (in == null || out == null || log == null) {
            throw new IllegalArgumentException();
        }
        this.in = in;
        this.out = out;
        this.log = log;
    }

    /**
     * Obtains all available items from the provided Input,
     * selects those which pass the filter
     * and sends them to the Output.
     * On success logs the result in format : 'Successfully selected X out of Y available products'
     * On failure logs the exception like : 'Filter procedure failed with exception: E'
     */
    public void select(Filter<Product> filter) {
        try {
            Collection<Product> allProducts = in.obtainProducts();
            Collection<Product> selectedProducts = select(allProducts, filter);
            out.postSelectedProducts(selectedProducts);
            logSuccess(allProducts, selectedProducts);
        } catch (ObtainFailedException e) {
            logFailure(e);
        }
    }

    private List<Product> select(Collection<Product> allProducts, Filter<Product> filter) {
        List<Product> selectedProducts = new ArrayList<>();
        for (Product product : allProducts) {
            if (filter.passes(product)) {
                selectedProducts.add(product);
            }
        }
        return selectedProducts;
    }

    private void logSuccess(Collection<Product> allProducts, Collection<Product> selectedProducts) {
        log.setLevel("INFO");
        log.log(TAG_CONTROLLER,
                "Successfully selected " + selectedProducts.size()
                + " out of " + allProducts.size() + " available products.");
    }

    private void logFailure(Exception exception) {
        log.setLevel("ERROR");
        log.log(TAG_CONTROLLER, "Filter procedure failed with exception: " + exception);
    }

}
