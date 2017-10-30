package app.news.com.newsapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import app.news.com.newsapp.Contants.NAConstant;
import app.news.com.newsapp.Pojo.News;
import app.news.com.newsapp.Presenter.NAHomeNewsPresenter;
import app.news.com.newsapp.Presenter.NAHomeNewsPresenterImpl;
import app.news.com.newsapp.Webservice.NAAPIInterface;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NAHomeNewsPresenterTest implements NAConstant {

    @Mock
    NAHomeNewsPresenter myView;
    @Mock
    NAAPIInterface myInteractor;

    private NAHomeNewsPresenterImpl myPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        myPresenter = new NAHomeNewsPresenterImpl(myView, myInteractor);
    }

    @Test
    public void WhileFirstTimeSearchCheckFooterHidedOrNot() {
        myPresenter.searchItems("sports", 0);
        verify(myView, times(1)).hideFooterProgress();
        verify(myView, never()).showFooterProgress();
    }


    @Test
    public void checkWebviewLoadingOrNot() {
        myPresenter.itemClick("https://android.jlelse.eu/android-mvp-basics-w-sample-app-3698e33ab9db");
        verify(myView, times(1)).loadWebview(anyString());
        verify(myView, never()).showFooterProgress();
        verify(myView, never()).hideFooterProgress();
        verify(myView, never()).showInitialProgress();
        verify(myView, never()).hideInitialProgress();
        verify(myView, never()).setItems(new News());
        verify(myView, never()).showMessage(SERVER_NOT_REACHABLE);
    }

    @Test
    public void ShowFooterProgressForNextpage() {
        myPresenter.onScroll(10, 10, 20, "sports");
        verify(myView, times(1)).showFooterProgress();
        verify(myView, never()).hideFooterProgress();
        verify(myView, never()).showInitialProgress();
    }


    @Test
    public void CheckPaginationWorkingOrNot() {
        myPresenter.onScroll(10, 10, 10, "sports");
        verify(myView, never()).showFooterProgress();
        verify(myView, never()).hideFooterProgress();
        verify(myView, never()).showInitialProgress();
    }

    @Test
    public void checkIfViewIsReleasedOnDestroy() {
        myPresenter.onDestroy();
        assertNull(myPresenter.getMainView());
    }

    @Test
    public void onFailureShowErrorMessage() {
        myPresenter.onFailure(null, null);
        verify(myView, never()).showFooterProgress();
        verify(myView, never()).hideFooterProgress();
        verify(myView, never()).showInitialProgress();
        verify(myView, never()).hideInitialProgress();
        verify(myView, never()).setItems(new News());
        verify(myView, times(1)).showMessage(SERVER_NOT_REACHABLE);
    }

}
