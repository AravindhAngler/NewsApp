package app.news.com.newsapp.Screens;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import app.news.com.newsapp.Contants.NAConstant;
import app.news.com.newsapp.FragmentManager.NAFragmentManager;
import app.news.com.newsapp.NetworkManager.NANetworkManager;
import app.news.com.newsapp.R;

public class NAScreenWebView extends Fragment implements NAConstant {
    private ProgressDialog myProgressDialog;
    private WebView myWebView;
    private View myView = null;
    private NAFragmentManager myFragmentManager;
    public static final String TAG = NAScreenWebView.class.getSimpleName();
    private FragmentActivity myContext;
    private NANetworkManager myNetworkManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Call xml layout here
        myView = inflater.inflate(R.layout.screen_webview, container, false);

        classAndWidgetInitialize(myView);

        return myView;


    }

    private void classAndWidgetInitialize(View aView) {

        myContext = getActivity();

        myFragmentManager = new NAFragmentManager(myContext);

        myNetworkManager = new NANetworkManager();

        myWebView = (WebView) myView.findViewById(R.id.screen_webview_WV);

        setWebView();

        loadInfo();

    }


    private void setWebView() {
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
    }

    private void loadInfo() {
        if (checkInternet()) {
            startAsyn();
        } else {

        }

    }


    private void startAsyn() {

        loadWebView();
    }

    private void loadWebView() {

        myProgressDialog = new ProgressDialog(getActivity());
        myProgressDialog.setCanceledOnTouchOutside(false);
        myProgressDialog.setMessage("Loading");
        myProgressDialog.show();

        // Get Arguments get values from another class
        Bundle aBundle = getArguments();
        if (aBundle != null) {

            //Add key values to aUrlStr
            String aUrlStr = aBundle.getString(CALL_URL);
            if (!aUrlStr.startsWith("http"))
                aUrlStr = "http://" + aUrlStr;

            Log.e(TAG, "WEBSITE URL " + aUrlStr);
            myWebView.getSettings().setJavaScriptEnabled(true);
            Log.d(TAG, "URL LOADING :" + aUrlStr);
            // Load the url
            myWebView.loadUrl(aUrlStr);
        }

        // Get Webview History
        myWebView.setFocusableInTouchMode(true);
        myWebView.requestFocus();
        myWebView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
                    myWebView.goBack();
                    return true;
                }
                return false;
            }
        });
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                myProgressDialog.dismiss();
            }
        });

    }

    /**
     * Check Intenet Connection
     *
     * @return true or false
     */
    private boolean checkInternet() {
        return myNetworkManager.isInternetOn(getActivity());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (myProgressDialog != null)
            myProgressDialog.dismiss();
    }
}
