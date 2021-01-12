package netdesigntool.com.eunions.wiki;

import com.squareup.moshi.Json;
import java.util.List;

/*
    POJO for parsing Wiki JSON response by Moshi.
 */
public class WikiResponse {

    @Json(name = "head")
    public Head head;
    @Json(name = "results")
    public Results results;


    public static class Head {
        @Json(name = "vars")
        public List<String> vars = null;
    }

    public static class Results {
        @Json(name = "bindings")
        public List bindings = null;
    }


    /*
            The Underneath is custom part of response.
     */

    public static class Binding {
        @Json(name = "iso")
        public TxtField iso;
        @Json(name = "contLabel")
        public TxtLangField contLabel;
        @Json(name = "capLabel")
        public TxtLangField capLabel;
    }



     public  static class TxtLangField {

            @Json(name = "xml:lang")
            public String xmlLang;
            @Json(name = "type")
            transient public String type;
            @Json(name = "value")
            public String value;
        }


    public static class TxtField {

            @Json(name = "type")
            transient public String type;
            @Json(name = "value")
            public String value;
        }

    }

