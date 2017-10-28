package app.news.com.newsapp.Screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.GrowEffect;

import java.util.ArrayList;
import java.util.List;

import app.news.com.newsapp.Adapter.NPNewsListAdapter;
import app.news.com.newsapp.Helper.NAHelper;
import app.news.com.newsapp.Helper.NAHideSoftKeyboard;
import app.news.com.newsapp.Pojo.Hit;
import app.news.com.newsapp.Pojo.News;
import app.news.com.newsapp.R;
import app.news.com.newsapp.Webservice.NAAPIClient;
import app.news.com.newsapp.Webservice.NAAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("NewApi")
public class NAHomeNewsFragment extends Fragment {
    // TAG
    public static final String TAG = NAHomeNewsFragment.class.getSimpleName().toString();

    private JazzyListView myNewsLV;
    private NPNewsListAdapter myAdapter;
    private LayoutInflater myInflater;
    private EditText mySearchEDT;
    private int MyTotalPage = 1;
    private RelativeLayout myInitialLoadingBarLay, myFooterLoadingLay;
    private TextView myNoDataFoundLay, myDoneTXT;
    private boolean isDataLoadedAlready = false;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private FragmentActivity myContext;
    private List<Hit> myNewsAL = new ArrayList<>();
    private NAAPIInterface myWebService;
    private int CURRENT_PAGE_COUNT = -1;

    private View myView;

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


            myInflater = (LayoutInflater) getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);

            myNewsLV = (JazzyListView) aView
                    .findViewById(R.id.screen_fragment_home_news_LV);

            myNewsLV.setShouldOnlyAnimateNewItems(true);

            myNewsLV.setTransitionEffect(new GrowEffect());

            myWebService =
                    NAAPIClient.getClient().create(NAAPIInterface.class);

            myInitialLoadingBarLay = (RelativeLayout) aView
                    .findViewById(R.id.screen_allreview_LAY_loading);

            myNoDataFoundLay = (TextView) aView
                    .findViewById(R.id.screen_fragment_home_news_TXT_no_data_found);

            mySearchEDT = (EditText) aView
                    .findViewById(R.id.screen_fragment_home_search_EDT);
            myDoneTXT = (TextView) aView
                    .findViewById(R.id.screen_fragment_home_search_IMG);

            myNoDataFoundLay.setText(getResources().getString(R.string.no_news_found));

            myFooterLoadingLay = (RelativeLayout) myInflater.inflate(
                    R.layout.layout_inflate_loading_details, null);

            mySwipeRefreshLayout = (SwipeRefreshLayout) aView
                    .findViewById(R.id.screen_fragment_home_news_SWRL);

            myAdapter = new NPNewsListAdapter(getActivity(),
                    myNewsAL);

            myNewsLV.setAdapter(myAdapter);

            myInitialLoadingBarLay.setVisibility(View.GONE);

            myFooterLoadingLay.setVisibility(View.GONE);

            configureListView();

            clickListeners();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clickListeners() {

        myDoneTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternet()) {
                    setSearchText(mySearchEDT.getText().toString().trim());
                    reloadAll();
                } else {
                    Toast.makeText(myContext, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * configure list view
     */
    private void configureListView() {

        try {
            myNewsLV.addFooterView(myFooterLoadingLay);

            myNewsLV.setOnScrollListener(new OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView arg0, int arg1) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    try {
                        if (myNewsAL != null)
                            if (myNewsAL.size() == 0)
                                return;

                        int lastInScreen = firstVisibleItem + visibleItemCount;
                        if (lastInScreen == totalItemCount) {

                            Log.e("Scroll last arrived", "Scroll last arrived "
                                    + getTotalPage());

                            if (checkInternet()) {

                                if ((getTotalPage() != 0)
                                        && (getTotalPage() > getCurrentPageCount())) {

                                    setFooterLoadingList(true);
                                    loadDetailsInfoFromServer();

                                } else {
                                    setFooterLoadingList(false);
                                    setInitialLoadingBarStatus(false);
                                }


                            } else {


                                setFooterLoadingList(false);
                                setInitialLoadingBarStatus(false);

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updatedData(List<Hit> aArrayList) {

        myAdapter.clear();

        if (aArrayList != null) {

            for (Hit aHit : aArrayList) {

                myAdapter.insert(aHit, myAdapter.getCount());
            }
        }

        myAdapter.notifyDataSetChanged();

    }

    /**
     * Set footer loading status
     *
     * @param aStatus
     */
    private void setFooterLoadingList(boolean aStatus) {

        try {
            if (aStatus)
                // Show footer
                myFooterLoadingLay.setVisibility(View.VISIBLE);
            else
                // Hide the footer
                myFooterLoadingLay.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Load details information
     */
    private void loadDetailsInfoFromServer() {

        if (checkInternet()) {
            increaePageCount();
            loadNewsDetails(getSearchText());
        } else {

        }
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
     * Set initial loading bar status
     *
     * @param aStatus
     */
    private void setInitialLoadingBarStatus(boolean aStatus) {

        try {

            if (aStatus)
                myInitialLoadingBarLay.setVisibility(View.VISIBLE);
            else
                myInitialLoadingBarLay.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Increase page number
     */
    private void increaePageCount() {

        setCurrentPageNo(getCurrentPageCount() + 1);

    }

    /**
     * Get total page
     *
     * @return total page
     */
    private int getTotalPage() {
        return MyTotalPage;
    }

    /**
     * Set total page
     *
     * @param aTotalPage
     */
    private void setTotalPage(int aTotalPage) {
        MyTotalPage = aTotalPage;
    }


    /**
     * Reload webservice
     */
    private void reloadAll() {

        // Clear cache
        myNewsAL.clear();

        updatedData(myNewsAL);

        // Show initial loading bar
        setInitialLoadingBarStatus(true);

        // Show footer loading
        setFooterLoadingList(false);

        // Hide initial loading bar
        setNoDataFoundStatus(false);

        // Reset page number
        setCurrentPageNo(-1);

        loadDetailsInfoFromServer();

    }

    private void setRefreshing(boolean aStatus) {
        if (aStatus) {
            mySwipeRefreshLayout.setRefreshing(true);
        } else {
            mySwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * loadListContent
     *
     * @param aAllReviewInfoList
     */
    private void loadListContent(List<Hit> aAllReviewInfoList) {

        // Hide initial loading bar
        setInitialLoadingBarStatus(false);

        if (aAllReviewInfoList.size() > 0) {

            if (!isDataLoadedAlready()) {
                myNewsAL = aAllReviewInfoList;
                updatedData(myNewsAL);
                setDataLoadedAlready(true);
                NAHelper.setColorScheme(mySwipeRefreshLayout);
                mySwipeRefreshLayout
                        .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                reloadAll();
                            }
                        });
            } else {
                for (int aCount = 0; aCount < aAllReviewInfoList.size(); aCount++)
                    addDetailsList(aAllReviewInfoList.get(aCount));
                updatedData(myNewsAL);
                mySwipeRefreshLayout
                        .setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                reloadAll();
                            }
                        });
            }

        } else {
            if (getCurrentPageCount() == 0)
                setNoDataFoundStatus(true);
            myFooterLoadingLay.setVisibility(View.GONE);
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
     * Set no data found layout status
     *
     * @param aStatus
     */
    private void setNoDataFoundStatus(boolean aStatus) {

        if (aStatus)
            myNoDataFoundLay.setVisibility(View.VISIBLE);
        else
            myNoDataFoundLay.setVisibility(View.GONE);

    }


    public int getCurrentPageCount() {
        return CURRENT_PAGE_COUNT;
    }

    public void setCurrentPageNo(int cURRENT_PAGE_COUNT) {
        CURRENT_PAGE_COUNT = cURRENT_PAGE_COUNT;
    }


    public void loadNewsDetails(String aSearchText) {


        Call<News> call = myWebService.getNews(aSearchText, CURRENT_PAGE_COUNT);
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                List<Hit> aHits = response.body().getHits();
                setRefreshing(false);
                setTotalPage(response.body().getNbPages());
                loadListContent(aHits);


            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    public String getSearchText() {
        return SearchText;
    }

    public void setSearchText(String searchText) {
        SearchText = searchText;
    }

    String SearchText = "";
}