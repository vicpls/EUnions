package netdesigntool.com.eunions.repo.wiki;

import androidx.annotation.Keep;

import com.squareup.moshi.Json;
import java.util.List;

/*
    POJO for parsing Wiki JSON-response by Moshi.
 */

@Keep
public class WikiResponse {

    @Json(name = "head")
    public Head head;
    @Json(name = "results")
    public Results results;

    @Keep
    public static class Head {
        @Json(name = "vars")
        public List<String> vars = null;
    }

    @Keep
    public static class Results {
        @Json(name = "bindings")
        public List bindings = null;
    }


    /*
            The Underneath is custom part of response.
     */

    @Keep
    public static class Binding {
        @Json(name = "iso")
        public TxtField iso;
        @Json(name = "contLabel")
        public TxtLangField contLabel;
        @Json(name = "capLabel")
        public TxtLangField capLabel;
    }


    @Keep
    public  static class TxtLangField {

            @Json(name = "xml:lang")
            public String xmlLang;
            @Json(name = "type")
            transient public String type;
            @Json(name = "value")
            public String value;
        }

    @Keep
    public static class TxtField {

            @Json(name = "type")
            transient public String type;
            @Json(name = "value")
            public String value;
        }

    }

