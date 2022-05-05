package netdesigntool.com.eunions.repo.wiki;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class WikiRxDataProviderTest {

    @Test
    public void getRespValue() {

        Map<String, String> innerMap = new HashMap(){
            {put("value","val1");}
        };

        Map<String, Map> otterMap = new HashMap(){
            {put("p1",innerMap);}
        };
        


        assertEquals(
                "val1"
                ,WikiRxDataProvider.getRespValue(otterMap,"p1"));

        assertEquals(
                ""
                ,WikiRxDataProvider.getRespValue(otterMap,"PP2"));
        // In the Log appears:    ERROR: EUnions: Cannot parse the value for param=PP2

    }
}