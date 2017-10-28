package app.news.com.newsapp.FragmentManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import app.news.com.newsapp.R;


public class NAFragmentManager {
    private FragmentActivity myContext;

    /**
     * Last fragment tag
     */
    public static String myLastTag = "";

    /**
     * Constructor to Initiate fragment manager
     *
     * @param aContext
     */
    public NAFragmentManager(FragmentActivity aContext) {
        myContext = aContext;
    }

    /**
     * Update the Current Fragment by passing the below parameters
     *
     * @param aFragment
     * @param tag
     * @param aBundle
     */
    public void updateContent(Fragment aFragment, String tag, Bundle aBundle) {
        try {

            Log.e("TAG Screen name", tag);

            // Initialise Fragment Manager
            final FragmentManager aFragmentManager = myContext
                    .getSupportFragmentManager();

            // Initialise Fragment Transaction
            final FragmentTransaction aTransaction = aFragmentManager
                    .beginTransaction();

            if (aBundle != null) {
                aFragment.setArguments(aBundle);
            }

            // Add the selected fragment
            aTransaction.add(R.id.main_container, aFragment, tag);

            // add the tag to the backstack
            aTransaction.addToBackStack(tag);

            // Commit the Fragment transaction
            aTransaction.commit();
            /*if (aFragment instanceof PKMFragment) {
                myContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            }*/
            myLastTag = tag;

            Log.i("LastTag", myLastTag);
        } catch (Exception aError) {
            aError.printStackTrace();
        }
    }



    public void backPress() {

        FragmentManager aFragmentManager = myContext
                .getSupportFragmentManager();

        if (aFragmentManager.getBackStackEntryCount() > 1) {
            aFragmentManager.popBackStack();
            aFragmentManager.executePendingTransactions();

            Log.d("TAG",
                    "CURRENT FRAGMENT BACK STACK CLASS "
                            + aFragmentManager
                            .getBackStackEntryAt(
                                    aFragmentManager
                                            .getBackStackEntryCount() - 1)
                            .getName());
        }
    }




}
