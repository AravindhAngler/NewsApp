package app.news.com.newsapp.NetworkManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by selvakumark on 28-10-2017.
 */
public class NANetworkManager {

    /**
     * Check the internet connection and return true or false
     *
     * @param aContext
     * @return
     */

    public final boolean isInternetOn(Context aContext) {
        try {

            if (aContext == null)
                return false;

            ConnectivityManager connectivityManager = (ConnectivityManager) aContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager
                    .getActiveNetworkInfo();
            return activeNetworkInfo != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}

