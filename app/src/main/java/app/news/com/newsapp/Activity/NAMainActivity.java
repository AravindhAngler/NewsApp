package app.news.com.newsapp.Activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.news.com.newsapp.FragmentManager.NAFragmentManager;
import app.news.com.newsapp.R;
import app.news.com.newsapp.Screens.NAHomeNewsFragment;

public class NAMainActivity extends AppCompatActivity {

    private NAFragmentManager myFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myFragmentManager = new NAFragmentManager(this);
        myFragmentManager.updateContent(new NAHomeNewsFragment(), NAHomeNewsFragment.TAG, null);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (myFragmentManager.getBackstackCount() == 1) {
            android.os.Process.killProcess(android.os.Process.myPid());
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }
}
