package app.news.com.newsapp.Webservice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NAAPIClient {

    public static final String BASE_URL = "https://hn.algolia.com/api/v1/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
