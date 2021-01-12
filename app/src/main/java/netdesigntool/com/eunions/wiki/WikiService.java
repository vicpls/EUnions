package netdesigntool.com.eunions.wiki;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WikiService {

    @Headers({
            //"Accept: application/sparql-results+xml,"
            "Accept: application/sparql-results+json"
            //,"Accept-Language: ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3"
    })
    @GET("sparql")
    Call<WikiResponse> wikiQuery(@Query("query") String sparqlQ);

    //Single<List<WikiResponse>> wikiRxQuery(@Query("query") String sparqlQ);
}
