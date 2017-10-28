package app.news.com.newsapp.Webservice;

/**
 * Created by selvakumark on 28-10-2017.
 */

import app.news.com.newsapp.Pojo.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NAAPIInterface {
    @GET("search?")
    Call<News> getNews(@Query("query") String apiKey,@Query("page") int aPage);


}
