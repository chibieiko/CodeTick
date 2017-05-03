package com.sankari.erika.codetick.Activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sankari.erika.codetick.ApiHandlers.ApiHandler;
import com.sankari.erika.codetick.ApiHandlers.UserHandler;
import com.sankari.erika.codetick.Classes.User;
import com.sankari.erika.codetick.Fragments.SectionsPagerAdapter;
import com.sankari.erika.codetick.Listeners.OnUserDataLoadedListener;
import com.sankari.erika.codetick.R;
import com.sankari.erika.codetick.Utils.Debug;
import com.sankari.erika.codetick.Utils.DownloadAndPlaceImage;
import com.sankari.erika.codetick.Utils.Urls;

public class MainActivity extends BaseActivity implements OnUserDataLoadedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ApiHandler handler;
    private UserHandler userHandler;
    private User user;
    private final String TAG = this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        Debug.loadDebug(this);
        Debug.print(TAG, "onCreate", "ON MAIN ACTIVITY CREATE", 5);

        handler = new ApiHandler(this);
        userHandler = new UserHandler(handler);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), handler);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set up tabs for ViewPager.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        System.out.println("MAIN ON START");
        if (userHandler.getUserListener() == null) {
            userHandler.addUserListener(this);
        }

        if (user == null) {
            userHandler.addUserListener(this);
            userHandler.getUserDetails(Urls.BASE_URL + "/users/current");
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (userHandler.getUserListener() == null) {
            userHandler.addUserListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (userHandler.getUserListener() != null) {
            userHandler.addUserListener(null);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (userHandler.getUserListener() != null) {
            userHandler.addUserListener(null);
        }

    }

    @Override
    public void onUserDataSuccessfullyLoaded(User obj) {
        user = obj;
        Debug.print(TAG, "onUserDataSuccessfullyLoaded", "USER IS: " + user, 5);

        final TextView userName = (TextView) findViewById(R.id.username);
        final TextView userEmail = (TextView) findViewById(R.id.user_email);
        new DownloadAndPlaceImage((ImageView) findViewById(R.id.user_image)).execute(user.getPhoto());

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (userName != null && userEmail != null) {
                    userName.setText(user.getName());
                    userEmail.setText(user.getEmail());
                }
            }
        });

    }

    @Override
    public void onUserDataLoadError(String error) {
        final String reason = error;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(findViewById(R.id.drawer_layout), reason, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Debug.print(TAG, "onUserDataLoadError", error, 5);
    }

}
