package netdesigntool.com.eunions.repo.wiki;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface WikiRxService {

    @Headers({ "Accept: application/sparql-results+json"  })
    @GET("sparql")
    Single<WikiResponse> wikiRxQuery(@Query("query") String sparqlQ);


}
