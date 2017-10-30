/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package app.news.com.newsapp.Presenter;

import android.os.Bundle;

import app.news.com.newsapp.Contants.NAConstant;
import app.news.com.newsapp.FragmentManager.NAFragmentManager;
import app.news.com.newsapp.NetworkManager.NANetworkManager;
import app.news.com.newsapp.Pojo.Hit;
import app.news.com.newsapp.Pojo.News;
import app.news.com.newsapp.Screens.NAScreenWebView;
import app.news.com.newsapp.Webservice.NAAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NAHomeNewsPresenterImpl implements NAMainPresenter, Callback<News>, NAConstant {

    private NAHomeNewsPresenter myMainView;
    private NAAPIInterface myWebservice;
    private NANetworkManager myNetworkManager;

    public NAHomeNewsPresenterImpl(NAHomeNewsPresenter mainView, NAAPIInterface aWebservice) {
        this.myMainView = mainView;
        this.myWebservice = aWebservice;
        this.myNetworkManager = new NANetworkManager();
    }

    @Override
    public void searchItems(String aSearchText, int aPage) {
        myMainView.hideFooterProgress();
        myMainView.showInitialProgress();
        Call<News> aCall = myWebservice.getNews(aSearchText, aPage);
        setCurrentPage(aPage);
        if (aCall != null)
            aCall.enqueue(this);
    }

    @Override
    public void onResponse(Call<News> call, Response<News> response) {
        myMainView.hideInitialProgress();
        setTotalPage(response.body().getNbPages());
        if (response.body().getHits().size() > 0) {
            myMainView.setItems(response.body());
        } else {
            myMainView.hideFooterProgress();
            if (getCurrentPage() == 0) {
                myMainView.showMessage(NO_DATA);
            }
        }
        setCurrentPage(getCurrentPage() + 1);
    }

    @Override
    public void onFailure(Call<News> call, Throwable t) {
        myMainView.showMessage(SERVER_NOT_REACHABLE);
    }

    @Override
    public void itemClick(String aUrl) {
        myMainView.loadWebview(aUrl);
    }

    @Override
    public void onScroll(int firstVisibleItem, int visibleItemCount, int totalItemCount, String aSearchtext) {

        int lastInScreen = firstVisibleItem + visibleItemCount;
        if (lastInScreen == totalItemCount) {
            if ((getTotalPage() != 0)
                    && (getTotalPage() > getCurrentPage())) {
                myMainView.showFooterProgress();
                Call<News> aCall = myWebservice.getNews(aSearchtext, getCurrentPage());
                if (aCall != null)
                    aCall.enqueue(this);
            } else {
                myMainView.hideFooterProgress();
                myMainView.hideInitialProgress();
            }

        }

    }

    @Override
    public void onDestroy() {
        this.myMainView = null;
    }

    public int getTotalPage() {
        return TotalPage;
    }

    public void setTotalPage(int totalPage) {
        TotalPage = totalPage;
    }

    private int TotalPage = 1;


    public int getCurrentPage() {
        return CurrentPage;
    }

    public void setCurrentPage(int currentPage) {
        CurrentPage = currentPage;
    }

    int CurrentPage = 0;

    public NAHomeNewsPresenter getMainView() {
        return myMainView;
    }



}
