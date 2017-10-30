package app.news.com.newsapp.Screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.GrowEffect;

import java.util.ArrayList;
import java.util.List;

import app.news.com.newsapp.Adapter.NPNewsListAdapter;
import app.news.com.newsapp.Contants.NAConstant;
import app.news.com.newsapp.FragmentManager.NAFragmentManager;
import app.news.com.newsapp.Helper.NAHelper;
import app.news.com.newsapp.Helper.NAHideSoftKeyboard;
import app.news.com.newsapp.Pojo.Hit;
import app.news.com.newsapp.Pojo.News;
import app.news.com.newsapp.Presenter.NAHomeNewsPresenter;
import app.news.com.newsapp.Presenter.NAHomeNewsPresenterImpl;
import app.news.com.newsapp.Presenter.NAMainPresenter;
import app.news.com.newsapp.R;
import app.news.com.newsapp.Webservice.NAAPIClient;
import app.news.com.newsapp.Webservice.NAAPIInterface;

@SuppressLint("NewApi")
public class NAHomeNewsFragment extends Fragment implements NAHomeNewsPresenter, NAConstant {
    // TAG
    public static final String TAG = NAHomeNewsFragment.class.getSimpleName().toString();

    private JazzyListView myNewsLV;
    private NPNewsListAdapter myAdapter;
    private LayoutInflater myInflater;
    private EditText mySearchEDT;
    private RelativeLayout myInitialLoadingBarLay, myFooterLoadingLay;
    private TextView myNoDataFoundLay, myDoneTXT;
    private boolean isDataLoadedAlready = false;
    private FragmentActivity myContext;
    private List<Hit> myNewsAL = new ArrayList<>();
    private View myView;
    private NAMainPresenter myNAMainPresenter;
    private NAFragmentManager myFragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.screen_fragment_home_news, container,
                false);

        NAHideSoftKeyboard.setupUI(
                myView.findViewById(R.id.screen_fragment_home_search_LAY_main),
                getActivity());

        classAndWidgetInitialize(myView);

        return myView;
    }

    private void classAndWidgetInitialize(View aView) {

        try {
            myContext = getActivity();

            myNAMainPresenter = new NAHomeNewsPresenterImpl(this, NAAPIClient.getClient().create(NAAPIInterface.class));

            myFragmentManager = new NAFragmentManager(myContext);

            myInflater = (LayoutInflater) getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            myNewsLV = (JazzyListView) aView
                    .findViewById(R.id.screen_fragment_home_news_LV);

            myNewsLV.setShouldOnlyAnimateNewItems(true);

            myNewsLV.setTransitionEffect(new GrowEffect());

            myInitialLoadingBarLay = (RelativeLayout) aView
                    .findViewById(R.id.screen_allreview_LAY_loading);

            myNoDataFoundLay = (TextView) aView
                    .findViewById(R.id.screen_fragment_home_news_TXT_no_data_found);

            mySearchEDT = (EditText) aView
                    .findViewById(R.id.screen_fragment_home_search_EDT);
            myDoneTXT = (TextView) aView
                    .findViewById(R.id.screen_fragment_home_search_IMG);

            myFooterLoadingLay = (RelativeLayout) myInflater.inflate(
                    R.layout.layout_inflate_loading_details, null);

            myAdapter = new NPNewsListAdapter(getActivity(),
                    myNewsAL);

            myNewsLV.setAdapter(myAdapter);

            clickListeners();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void showInitialProgress() {
        myNoDataFoundLay.setVisibility(View.GONE);
        myInitialLoadingBarLay.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFooterProgress() {
        myFooterLoadingLay.setVisibility(View.VISIBLE);

    }

    @Override
    public void loadWebview(String aUrl) {
        if (checkInternet()) {
            callWebView(aUrl);
        } else {
            showToastMessage(NO_INTERNET);
        }
    }

    @Override
    public void hideInitialProgress() {
        myInitialLoadingBarLay.setVisibility(View.GONE);
    }

    @Override
    public void hideFooterProgress() {
        myFooterLoadingLay.setVisibility(View.GONE);
    }

    @Override
    public void setItems(News aNews) {
        List<Hit> aHits = aNews.getHits();
        loadListContent(aHits);
    }

    @Override
    public void showMessage(String message) {
        myAdapter.clear();
        myNewsAL = new ArrayList<>();
        myNoDataFoundLay.setVisibility(View.VISIBLE);
        myNoDataFoundLay.setText(message);
    }


    @Override
    public void showToastMessage(String message) {
        Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        myNAMainPresenter.onDestroy();
        super.onDestroy();
    }

    private void clickListeners() {

        myDoneTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternet()) {
                    myAdapter.clear();
                    myNewsAL = new ArrayList<>();
                    myNAMainPresenter.searchItems(mySearchEDT.getText().toString().trim(), 0);

                } else {
                    showMessage(NO_INTERNET);
                }
            }
        });
        myNewsLV.addFooterView(myFooterLoadingLay);

        myNewsLV.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (checkInternet()) {
                    if (myNewsAL.size() != 0) {
                        myNAMainPresenter.onScroll(firstVisibleItem, visibleItemCount, totalItemCount, mySearchEDT.getText().toString().trim());
                    }
                } else {
                    hideFooterProgress();
                    hideInitialProgress();
                    showToastMessage(NO_INTERNET);
                }
            }
        });

        myNewsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (checkInternet()) {
                    Hit aHit = (Hit) parent.getItemAtPosition(position);
                    myNAMainPresenter.itemClick(aHit.getUrl());
                } else {
                    showToastMessage(NO_INTERNET);
                }
            }
        });
    }


    /**
     * loadListContent
     *
     * @param aAllReviewInfoList
     */

    private void loadListContent(List<Hit> aAllReviewInfoList) {

        if (!isDataLoadedAlready()) {
            myNewsAL = aAllReviewInfoList;
            myAdapter.updatedData(myNewsAL);
            setDataLoadedAlready(true);
        } else {
            for (int aCount = 0; aCount < aAllReviewInfoList.size(); aCount++)
                addDetailsList(aAllReviewInfoList.get(aCount));
            myAdapter.updatedData(myNewsAL);
        }

    }

    private void addDetailsList(Hit aValueData) {
        myNewsAL.add(aValueData);
    }

    /**
     * Check data loaded before or not
     *
     * @return true - Data loaded already<br>
     * false - Data not loaded
     */
    private boolean isDataLoadedAlready() {
        return isDataLoadedAlready;
    }

    /**
     * Set Data loaded status
     *
     * @param aIsDataLoadedAlready
     */
    private void setDataLoadedAlready(boolean aIsDataLoadedAlready) {
        this.isDataLoadedAlready = aIsDataLoadedAlready;
    }

    /**
     * check internet
     *
     * @return
     */
    private boolean checkInternet() {
        return NAHelper.checkInternet(getActivity());
    }

    /**
     * Method to call webview
     *
     * @param aUrlStr
     */
    private void callWebView(String aUrlStr) {
        try {
            if (!aUrlStr.equals(""))

                try {
                    Bundle aBundle = new Bundle();
                    aBundle.putString(CALL_URL, aUrlStr);
                    myFragmentManager.updateContent(
                            new NAScreenWebView(),
                            NAScreenWebView.TAG, aBundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}