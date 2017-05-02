package cz.muni.fi.pv260.productfilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by micha on 02.05.2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    @Mock
    Product product1;

    @Mock
    Product product2;

    @Mock
    Product product3;

    @Mock
    Product product4;

    @Mock
    Product product5;

    @Mock
    Input input;

    @Mock
    Output output;

    @Mock
    Logger logger;

    @Captor
    ArgumentCaptor<Collection<Product>> productsCaptor;

    @Mock
    Filter<Product> filter;

    @Test(expected = IllegalArgumentException.class)
    public void testConstructAllNull(){
        Controller controller = new Controller(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructInNull(){
        Controller controller = new Controller(null, output, logger);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructOutNull(){
        Controller controller = new Controller(input, null, logger);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructLogNull(){
        Controller controller = new Controller(input, output, null);
    }

    private static void mockFilter(Filter<Product> mock, Product... validProducts) {
        when(mock.passes(any())).thenReturn(false);
        for (Product p : validProducts) {
            when(mock.passes(p)).thenReturn(true);
        }
    }

    @Test
    public void testBasicFunctionality() throws ObtainFailedException {
        when(input.obtainProducts()).thenReturn(Arrays.asList(product1, product2, product3, product4, product5));
        Controller controller = new Controller(input, output, logger);
        mockFilter(filter, product1, product2, product3);

        controller.select(filter);

        verify(output).postSelectedProducts(productsCaptor.capture());
        Collection<Product> capturedProducts = productsCaptor.getValue();
        assertEquals("Controller should have selected 3 products", 3,  capturedProducts.size());
        assertTrue("Selected items contain product1", capturedProducts.contains(product1));
        assertTrue("Selected items contain product2", capturedProducts.contains(product2));
        assertTrue("Selected items contain product3", capturedProducts.contains(product3));

        verify(logger).setLevel(eq("INFO"));
        verify(logger).log(any(), eq("Successfully selected 3 out of 5 available products."));
    }

    @Test
    public void testEmptyFilter() throws ObtainFailedException {
        when(input.obtainProducts()).thenReturn(Arrays.asList(product1, product2, product3, product4, product5));
        Controller controller = new Controller(input, output, logger);
        mockFilter(filter);

        controller.select(filter);

        verify(output).postSelectedProducts(productsCaptor.capture());
        Collection<Product> capturedProducts = productsCaptor.getValue();
        assertEquals("Controller should have selected 0 products", 0,  capturedProducts.size());

        verify(logger).setLevel(eq("INFO"));
        verify(logger).log(any(), eq("Successfully selected 0 out of 5 available products."));
    }


    @Test
    public void testAllFilter() throws ObtainFailedException {
        when(input.obtainProducts()).thenReturn(Arrays.asList(product1, product2, product3, product4, product5));
        Controller controller = new Controller(input, output, logger);
        mockFilter(filter, product1, product2, product3, product4, product5);

        controller.select(filter);

        verify(output).postSelectedProducts(productsCaptor.capture());
        Collection<Product> capturedProducts = productsCaptor.getValue();
        assertEquals("Controller should have selected 5 products", 5,  capturedProducts.size());
        assertTrue("Selected items contain product1", capturedProducts.contains(product1));
        assertTrue("Selected items contain product2", capturedProducts.contains(product2));
        assertTrue("Selected items contain product3", capturedProducts.contains(product3));
        assertTrue("Selected items contain product4", capturedProducts.contains(product4));
        assertTrue("Selected items contain product5", capturedProducts.contains(product5));

        verify(logger).setLevel(eq("INFO"));
        verify(logger).log(any(), eq("Successfully selected 5 out of 5 available products."));
    }


    @Test
    public void testObtainException() throws ObtainFailedException {
        ObtainFailedException ex = mock(ObtainFailedException.class);
        when(ex.toString()).thenReturn("Mocked exception");

        when(input.obtainProducts()).thenThrow(ex);
        Controller controller = new Controller(input, output, logger);
        controller.select(filter);

        verify(logger).setLevel(eq("ERROR"));
        verify(logger).log(any(), eq("Filter procedure failed with exception: Mocked exception"));
    }

}