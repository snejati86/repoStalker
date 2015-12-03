package com.bypassmobile.octo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bypassmobile.octo.R;
import com.bypassmobile.octo.di.AppComponent;
import com.bypassmobile.octo.model.User;
import com.bypassmobile.octo.rest.GithubEndpoint;

import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity implements OnUserClicked
{
    /**
     * TAG to indentify this class for logging.
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.my_recycler_view)
    RecyclerView userList;

    @Bind(R.id.current_user)
    TextView currentUser;

    @Bind(R.id.progress_overlay)
    FrameLayout progressBarOverlay;

    @Inject
    GithubEndpoint githubEndpoint;

    @Inject
    ConnectivityManager connectivityManager;

    RecyclerView.LayoutManager mLayoutManager;

    GithubUserAdapter mAdapter;

    private Stack<User> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if ( connectivityManager.getActiveNetworkInfo() != null )
        {
            history = new Stack<User>();
            userList.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(this);
            userList.setLayoutManager(mLayoutManager);

            mAdapter = new GithubUserAdapter(this,this);
            userList.setAdapter(mAdapter);
            setAnimation(0f, 1f, View.VISIBLE);
            history.add(new User("bypasslane","fakeURL"));
            githubEndpoint.getOrganizationMember("bypasslane", new Callback<List<User>>() {
                @Override
                public void success(List<User> users, Response response) {
                    setAnimation(1f, 0f, View.GONE);
                    currentUser.setText("bypasslane");
                    for (User user : users) {
                        mAdapter.addUser(user);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    setAnimation(1f, 0f, View.GONE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Github unavailable, Try back later.")
                            .setTitle("Github Error").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    builder.create().show();
                }
            });
        }
        else
        {
            Log.d(TAG, "Offline.");
        }
    }

    private void setAnimation(float fromAlpha, float toAlpha, int visible) {
        Animation inAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        inAnimation.setDuration(200);
        progressBarOverlay.setAnimation(inAnimation);
        progressBarOverlay.setVisibility(visible);
    }

    @Override
    void inject(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setUser(User user) {
        setCurrentUser(user);
    }

    private void setCurrentUser(final User user) {
        setAnimation(0f, 1f, View.VISIBLE);
        githubEndpoint.getFollowingUser(user.getName(), new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                setAnimation(1f, 0f, View.GONE);

                if (users.size() > 0) {

                    history.push(user);
                    currentUser.setText(user.getName());
                    mAdapter.clear();
                    for (User user : users) {
                        mAdapter.addUser(user);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No followers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                setAnimation(1f, 0f, View.GONE);

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (! history.isEmpty()){
            setCurrentUser(history.pop());
        }
        else{
            finish();
        }
    }

}
