package app.news.com.newsapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import app.news.com.newsapp.Contants.NAConstant;
import app.news.com.newsapp.FragmentManager.NAFragmentManager;
import app.news.com.newsapp.Helper.NAHelper;
import app.news.com.newsapp.Pojo.Hit;
import app.news.com.newsapp.R;
import app.news.com.newsapp.Screens.NAScreenWebView;


public class NPNewsListAdapter extends ArrayAdapter<Hit> implements NAConstant {

    private Context myContext;
    private List<Hit> myNewsList;
    private LayoutInflater myInflater;
    private NAFragmentManager myFragmentManager;

    public NPNewsListAdapter(FragmentActivity aContext, List<Hit> aArrayList) {
        super(aContext, 0, aArrayList);
        this.myNewsList = aArrayList;
        myFragmentManager = new NAFragmentManager(aContext);
        this.myContext = aContext;
    }

    private List<Hit> getMyArrayList() {
        return this.myNewsList;
    }

    @Override
    public int getCount() {
        return myNewsList.size();
    }

    @Override
    public Hit getItem(int position) {
        return myNewsList.get(position);
    }

    @Override
    public long getItemId(int aPosition) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public View getView(final int aPosition, View aView, ViewGroup aViewGroup) {

        final ViewHolder aHolder;
        if (aView == null) {
            myInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            aView = myInflater.inflate(R.layout.layout_inflate_news_list_adapter, aViewGroup, false);
            aHolder = new ViewHolder();
            aHolder.myTitleTXT = (TextView) aView
                    .findViewById(R.id.layout_inflate_news_list_title);
            aHolder.myStoryTXT = (TextView) aView
                    .findViewById(R.id.layout_inflate_news_list_story);
            aHolder.myTimeTTX = (TextView) aView
                    .findViewById(R.id.layout_inflate_news_list_time);
            aHolder.myAuthorTXT = (TextView) aView
                    .findViewById(R.id.layout_inflate_news_list_author);
            aView.setTag(aHolder);
        } else {
            aHolder = (ViewHolder) aView.getTag();
        }

        loadValues(aHolder, aPosition);

        clickListener(aView, aPosition);

        return aView;
    }

    private void clickListener(View aView, final int aPosition) {
        aView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWebView(getItem(aPosition).getUrl());
            }
        });
    }

    private void loadValues(ViewHolder aHolder, int aPosition) {
        try {
            aHolder.myTitleTXT.setText(getItem(aPosition).getTitle());
            aHolder.myAuthorTXT.setText("Author - " + getItem(aPosition).getAuthor());
            aHolder.myTimeTTX.setText(NAHelper.getDate(getItem(aPosition).getCreatedAtI(), NEWS_TIME_FORMAT));
            if (getItem(aPosition).getStoryText() == null) {
                aHolder.myStoryTXT.setVisibility(View.GONE);
            } else {
                aHolder.myStoryTXT.setText(getItem(aPosition).getStoryText());
                aHolder.myStoryTXT.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * View Holder
     */
    class ViewHolder {
        private TextView myTitleTXT, myStoryTXT, myAuthorTXT, myTimeTTX;

    }

    /**
     * Method to call webview
     *
     * @param aUrlStr
     */
    private void callWebView(String aUrlStr) {
        try {
            if (!aUrlStr.equals(""))
                if (checkInternet()) {
                    try {
                        Bundle aBundle = new Bundle();
                        aBundle.putString(CALL_URL, aUrlStr);
                        myFragmentManager.updateContent(
                                new NAScreenWebView(),
                                NAScreenWebView.TAG, aBundle);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(myContext, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            // RTCHelper.callWebView(myContext, aUrlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * check internet
     *
     * @return
     */
    private boolean checkInternet() {
        return NAHelper.checkInternet(myContext);
    }
}

