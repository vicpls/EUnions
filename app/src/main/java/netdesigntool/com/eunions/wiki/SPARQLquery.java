package netdesigntool.com.eunions.wiki;

import android.annotation.SuppressLint;

import java.util.Locale;

/*
    The stattic string of SPARQL Queries to Wiki.
 */
public final class SPARQLquery {

    final static String BASE_URL = "https://query.wikidata.org/";

    @SuppressLint("ConstantLocale")
    final static String ISO_LANG = Locale.getDefault().getLanguage() +",en";

    public final static String POP_ID ="P1082";
    public final static String AREA_ID="P2046";


    /*
     Return: Capital, Area, Currency
     Param:
        String - ISO2 Code of Country
        string - local (e.g. ="[AUTO_LANGUAGE],en" without quotation marks)
      */
    final static String CAP_CUR_AREA =
        "SELECT ?PrCapLabel ?CapLabel ?PrAreaLabel ?Area ?ArUnitSymbols ?PrCurLabel ?CurrencyLabel WHERE {" +

            " BIND (\"%S\" as ?iso)" +
            " ?cont wdt:P31* wd:Q3624078; wdt:P297 ?iso" +

            " OPTIONAL { ?cont wdt:P36 ?Cap}" +
            " OPTIONAL { ?cont p:P2046/psv:P2046 ["+
                " wikibase:quantityAmount ?Area; wikibase:quantityUnit ?AreaUnit ]}" +
            " OPTIONAL { ?cont wdt:P38 ?Currency}" +

            " BIND (wd:P36 as ?PrCap)" +
            " BIND (wd:P2046 as ?PrArea)" +
            " BIND (wd:P38 as ?PrCur)" +

            " OPTIONAL { ?AreaUnit p:P5061/ps:P5061 ?ArUnitSymbols }" +
            " FILTER (LANG(?ArUnitSymbols)=\"en\")" +

            " SERVICE wikibase:label { bd:serviceParam wikibase:language \"%s\" }}";


    /*
     Return: List of country's Membership in unions
     Param:
        String - ISO2 Code of Country
        string - local (e.g. ="[AUTO_LANGUAGE],en" without quotation marks)
      */
    final static String MEMBER =
            "SELECT ?quaLabel  WHERE {" +

                    " BIND (\"%S\" as ?iso)" +
                    "  ?cont wdt:P31* wd:Q3624078." +
                    "  ?cont wdt:P297 ?iso." +

                    "  ?cont p:P463 ?stm." +
                    "  VALUES ?qua {wd:Q458 wd:Q7184 wd:Q7825 wd:Q8908}" +
                    "  ?stm ps:P463 ?qua. " +

                    "  FILTER NOT EXISTS {?stm pq:P582 ?endData}" +

                    "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"%s\". }}";



    final static String NAME_OF_MEMBER_PREDICATE =
            "";

    /*
        Return: Country's total GDP per capita
        Param:
           String - ISO2 Code of Country
           string - local (e.g. ="[AUTO_LANGUAGE],en" without quotation marks)
     */
    final static String GDP_PER_CAPITA =
            "SELECT ?last_gdp ?gdplLabel ?lyear ?gdpUnitSymbols WHERE {" +

                    " BIND (\"%S\" as ?iso)" +
                    "  ?cont wdt:P31* wd:Q3624078." +
                    "  ?cont wdt:P297 ?iso." +

                    " ?cont p:P2132 ?gdpp." +
                    " ?gdpp pq:P585 ?year." +
                    " BIND (MAX(?year) as ?ldate)"+

                    " ?gdpp pq:P585 ?ldate;" +
                    " ps:P2132 ?last_gdp" +

                    " BIND (YEAR(?ldate) as ?lyear)"+
                    " BIND (wd:P2132 as ?gdpl)"+

                    " OPTIONAL{?gdpp psv:P2132 [wikibase:quantityUnit ?gdpUnit]."+
                    " ?gdpUnit p:P5061/ps:P5061 ?gdpUnitSymbols}"+

                    "  SERVICE wikibase:label { bd:serviceParam wikibase:language \"%s\". }}";


    /*
        Return: Last available Country's Human Development Index, year, title in selected language.
     */
    final static String HUM_DEV_IND =
            "SELECT ?last_hdi ?hdiLabel ?lyear WHERE {" +

                    " BIND (\"%S\" as ?iso)" +
                    "  ?cont wdt:P31* wd:Q3624078." +
                    "  ?cont wdt:P297 ?iso." +

                    " ?cont p:P1081 ?gdpp." +
                    " ?gdpp pq:P585 ?year." +
                    " BIND (MAX(?year) as ?ldate)"+

                    " ?gdpp pq:P585 ?ldate;" +
                    " ps:P1081 ?last_hdi" +

                    " BIND (YEAR(?ldate) as ?lyear)"+
                    " BIND (wd:P1081 as ?hdi)"+

                    " SERVICE wikibase:label { bd:serviceParam wikibase:language \"%s\"}}";

    /*
        Return: Last available population of country,  year, title in selected language.
     */
    final static String POPULATION =
            "SELECT ?last_pop ?poptLabel ?lyear WHERE {" +

                    " BIND (\"%S\" as ?iso) " +
                    " ?cont wdt:P31* wd:Q3624078. " +
                    " ?cont wdt:P297 ?iso. " +
                    " ?cont p:P1082 ?pop. " +
                    " ?pop pq:P585 ?years. " +

                    " BIND (MAX(?years) as ?ldate) " +

                    " ?pop pq:P585 ?ldate; ps:P1082 ?last_pop " +

                    " BIND (YEAR(?ldate) as ?lyear) " +
                    " BIND (wd:P1082 as ?popt) " +

                    " SERVICE wikibase:label { bd:serviceParam wikibase:language \"%s\"}}";

}
