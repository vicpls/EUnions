package netdesigntool.com.eunions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import netdesigntool.com.eunions.repo.wiki.HumanReadableNumber;
import netdesigntool.com.eunions.repo.wiki.Parameter;

public class ParameterTest {

    Parameter parameter;

    private static final String
            WIKI_ID ="wikiId"
            , PARAMETER_NAME ="ParameterName"
            , QUALIFIER ="Qualifier"
            , DATE_2021 ="Date_2021"
            , VALUE ="Value"
            , UNIT ="Unit"
            , PREFIX ="prefix";

    {
        parameter = new Parameter(
                WIKI_ID
                , PARAMETER_NAME
                , QUALIFIER
                , DATE_2021
                , VALUE
                , true
                , UNIT
        );
    }

    @Test
    public void set_get_HrProvider(){

        HumanReadableNumber hrn =
        new HumanReadableNumber() {
            @Override
            public String roundNumber(String value, int accuracy) {
                return "";
            }
            @Override
            public String getPrefixForNumber(String value) {
                return "";
            }
        };

        Parameter.setHrProvider(hrn);
        assertEquals(hrn, Parameter.getHrProvider());
        Parameter.setHrProvider(null);
        assertNotEquals(hrn, Parameter.getHrProvider());
    }


    @Test
    public void getQualifiedSting() {

        String result = QUALIFIER +" "
                + UNIT +" "
                +", "
                +"("+ DATE_2021 +")";

        assertEquals(result, parameter.getQualifiedSting());
    }

    @Test
    public void test_getHumanReadableValue() {

        Parameter.setHrProvider(
                new HumanReadableNumber() {
                    @Override
                    public String roundNumber(String value, int accuracy) {
                        return value + accuracy;
                    }
                    @Override
                    public String getPrefixForNumber(String value) {
                        return PREFIX;
                    }
                }
        );

        assertEquals(VALUE+"1 "+ PREFIX
                , parameter.getHumanReadableValue());
    }

}