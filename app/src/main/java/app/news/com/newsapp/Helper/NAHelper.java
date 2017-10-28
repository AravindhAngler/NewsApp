package app.news.com.newsapp.Helper;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

import app.news.com.newsapp.NetworkManager.NANetworkManager;
import app.news.com.newsapp.R;

/**
 * Created by selvakumark on 28-10-2017.
 */

public class NAHelper {

    public static boolean checkInternet(Activity aContext) {
        return new NANetworkManager().isInternetOn(aContext);

    }


    public static boolean checkInternet(Context aContext) {
        return new NANetworkManager().isInternetOn(aContext);

    }

    /**
     * setColorScheme for Refresh Layout
     *
     * @param aSwipeRefreshLayout
     */
    public static void setColorScheme(SwipeRefreshLayout aSwipeRefreshLayout) {
        aSwipeRefreshLayout.setColorScheme(R.color.colorAccent, R.color.colorPrimary,
                R.color.orange, R.color.orange_disable);

    }

    /**
     * Check string is integer
     *
     * @param s
     * @return
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);

        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static String getDate(long time, String aFormmat) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format(aFormmat, cal).toString();
        return date;
    }


}
