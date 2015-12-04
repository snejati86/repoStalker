
package com.bypassmobile.octo.activities;

import java.util.List;
import java.util.Stack;

import javax.inject.Inject;
import javax.inject.Named;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.bypassmobile.octo.R;
import com.bypassmobile.octo.di.AppComponent;
import com.bypassmobile.octo.model.User;
import com.bypassmobile.octo.rest.GithubEndpoint;

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

    @Inject
    @Named("Fade_in")
    Animation fadeInAnimation;

    @Inject
    @Named("Fade_out")
    Animation fadeOutAnimation;

    RecyclerView.LayoutManager mLayoutManager;

    GithubUserAdapter mAdapter;

    private Callback<List<User>> callback = new Callback<List<User>>() {
        @Override
        public void success(List<User> users, Response response) {
            showProgressBar(false);
            mAdapter.clear();
            for (User user : users) {
                mAdapter.addUser(user);
            }
            if ( users.size() == 0 ){
                Toast.makeText(getApplicationContext(), R.string.not_following_message,Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void failure(RetrofitError error) {
            showProgressBar(false);
            popPositiveDialog(getString(R.string.general_github_error), getString(R.string.title_github_error));

        }
    };

    /**
     * Unbounded history, TODO : Get requirement for max users.
     */
    private Stack<User> history;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (connectivityManager.getActiveNetworkInfo() != null)
        {
            history = new Stack<User>();
            userList.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            userList.setLayoutManager(mLayoutManager);

            mAdapter = new GithubUserAdapter(getApplicationContext(), this);
            userList.setAdapter(mAdapter);
            showProgressBar(true);
            final User object = new User(getString(R.string.ROOT_ACCOUNT), "MARKER");
            setUser(object);

        }
        else
        {
            Log.d(TAG, "Offline.");
        }
    }


    @Override
    void inject(AppComponent appComponent)
    {
        appComponent.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUser(User user)
    {
        setCurrentUser(user);
    }

    /**
     * This method will set the current user, for the page.
     * @param user
     */
    private void setCurrentUser(final User user)
    {
        showProgressBar(true);
        history.add(user);
        currentUser.setText(user.getName());
        if (! user.getName().equals(getString(R.string.ROOT_ACCOUNT))) {
            githubEndpoint.getFollowingUser(user.getName(), callback);
        }
        else {
            githubEndpoint.getOrganizationMember(user.getName(),callback);
        }
    }

    @Override
    public void onBackPressed()
    {
        if (!history.isEmpty())
        {
            history.pop();
            //OFF WITH THE CURRENT USER.
            if (!history.isEmpty())
            {
                setCurrentUser(history.pop());
            }
            else
            {
                finish();
            }
        }
        else
        {
            finish();
        }
    }

    /**
     * Method that pops an OK dialog.
     * 
     * @param message Message to be shown
     * @param title Alert Dialog title.
     */
    private void popPositiveDialog(String message, String title)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message).setTitle(title)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.create().show();
    }

    /**
     * This method will pop up a progressbar overlaying the screen.
     * @param setVisibile
     */
    private void showProgressBar(boolean setVisibile)
    {
        if (setVisibile)
        {
            progressBarOverlay.setVisibility(View.VISIBLE);
            progressBarOverlay.startAnimation(fadeInAnimation);
        }
        else
        {
            progressBarOverlay.startAnimation(fadeOutAnimation);
            progressBarOverlay.setVisibility(View.GONE);
        }
    }

}
