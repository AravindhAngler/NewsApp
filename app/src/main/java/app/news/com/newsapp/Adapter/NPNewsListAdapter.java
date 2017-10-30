package app.news.com.newsapp.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.news.com.newsapp.Contants.NAConstant;
import app.news.com.newsapp.FragmentManager.NAFragmentManager;
import app.news.com.newsapp.Helper.NAHelper;
import app.news.com.newsapp.Pojo.Hit;
import app.news.com.newsapp.R;


public class NPNewsListAdapter extends ArrayAdapter<Hit> implements NAConstant {

    private Context myContext;
    private List<Hit> myNewsList;
    private LayoutInflater myInflater;

    public NPNewsListAdapter(FragmentActivity aContext, List<Hit> aArrayList) {
        super(aContext, 0, aArrayList);
        this.myNewsList = aArrayList;
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

        return aView;
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

    public void updatedData(List<Hit> aArrayList) {
        clear();
        if (aArrayList != null) {
            for (Hit aHit : aArrayList) {
                insert(aHit, getCount());
            }
        }
        notifyDataSetChanged();
    }

    /**
     * View Holder
     */
    class ViewHolder {
        private TextView myTitleTXT, myStoryTXT, myAuthorTXT, myTimeTTX;

    }

}

