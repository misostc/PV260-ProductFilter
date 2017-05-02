package unit;

import cz.muni.fi.pv260.productfilter.AtLeastNOfFilter;
import cz.muni.fi.pv260.productfilter.Filter;
import cz.muni.fi.pv260.productfilter.FilterNeverSucceeds;
import org.junit.Assert;
import org.junit.Test;


public class AtLeastNOfFilterTest {

    private AtLeastNOfFilter<String> filter;


    @Test(expected = IllegalArgumentException.class)
    public void testConstructNZero(){
        filter = new AtLeastNOfFilter<>(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNLessThanZero(){
        filter = new AtLeastNOfFilter<>(-2);
    }

    @Test(expected = FilterNeverSucceeds.class)
    public void testConstructNGreaterThanCountOfFilters(){
        filter = new AtLeastNOfFilter<>(Integer.MAX_VALUE);
    }

    private class FilterNonEmptyString implements Filter<String> {

        @Override
        public boolean passes(String item) {
            return !item.isEmpty();
        }
    }

    @Test
    public void testConstructValid(){
        filter = new AtLeastNOfFilter<>(1,new FilterNonEmptyString());
        Assert.assertNotNull("Filter should be initialized",filter);
    }

    @Test
    public void testPassesInvalidSingleFilter(){
        filter = new AtLeastNOfFilter<>(1,new FilterNonEmptyString());
        Assert.assertFalse("filter::passes should fail on empty string",filter.passes(""));
    }

    @Test
    public void testPassesValidSingleFilter(){
        filter = new AtLeastNOfFilter<>(1,new FilterNonEmptyString());
        Assert.assertTrue("filter:passes should succeed on non-empty string",filter.passes("Hello World!"));
    }

    private class FilterStringStartsWithH implements Filter<String>{

        @Override
        public boolean passes(String item) {
            return item.startsWith("H");
        }
    }

    @Test
    public void testPassesInvalidMultipleChildFilters(){
        filter = new AtLeastNOfFilter<>(2,new FilterNonEmptyString(),new FilterStringStartsWithH());
        Assert.assertFalse("filter::passes should fail because not all filter passes",filter.passes("Welcome"));
    }

    private class FilterSingleCharacterString implements Filter<String>{

        @Override
        public boolean passes(String item) {
            return item.length() == 1;
        }
    }

    @Test
    public void testPassesAtMostZeroChildFiltersPasses(){
        filter = new AtLeastNOfFilter<>(1,new FilterSingleCharacterString(),new FilterStringStartsWithH());
        Assert.assertFalse("filter:passes should fail because n - 1 filters passes only",filter.passes("Welcome"));
    }

    @Test
    public void testPassesSingleChildFilterPasses(){
        filter = new AtLeastNOfFilter<>(1,new FilterSingleCharacterString(),new FilterStringStartsWithH());
        Assert.assertTrue("filter:passes should pass because at least n filters passes",filter.passes("Hello"));
    }

    @Test
    public void testPassesAllChildFilterPasses(){
        filter = new AtLeastNOfFilter<>(1,new FilterNonEmptyString(),new FilterSingleCharacterString(), new FilterStringStartsWithH());
        Assert.assertTrue("filter:passes should pass because all child filter passes",filter.passes("H"));
    }

    @Test
    public void testPassesValidMultipleChildFilters(){
        filter = new AtLeastNOfFilter<>(2,new FilterNonEmptyString(),new FilterNonEmptyString(), new FilterStringStartsWithH());
        Assert.assertTrue("filter:passes should pass because number of passed filters is atleast n",filter.passes("Hello World!"));
    }
}
