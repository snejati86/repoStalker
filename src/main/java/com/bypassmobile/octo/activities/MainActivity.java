package com.bypassmobile.octo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bypassmobile.octo.R;
import com.bypassmobile.octo.application.ByPassApplication;
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

public class MainActivity extends Activity implements OnUserClicked
{
    @Bind(R.id.my_recycler_view)
    RecyclerView userList;

    @Bind(R.id.current_user)
    TextView currentUser;

    RecyclerView.LayoutManager mLayoutManager;

    GithubUserAdapter mAdapter;

    @Inject
    GithubEndpoint githubEndpoint;

    private Stack<User> usersStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usersStack = new Stack<User>();
        ButterKnife.bind(this);
        ((ByPassApplication)getApplication()).getAppComponent().inject(this);
        userList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        userList.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new GithubUserAdapter(this,this);
        userList.setAdapter(mAdapter);
        githubEndpoint.getOrganizationMember("bypasslane", new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                currentUser.setText("bypasslane");
                for (User user : users) {
                    mAdapter.addUser(user);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                throw error;
            }
        });
        //mAdapter.addUser(new User("Meing","https://avatars3.githubusercontent.com/u/13679?v=3&s=96"));
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
        githubEndpoint.getFollowingUser(user.getName(), new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                if ( users.size() > 0 ){
                usersStack.push(user);
                currentUser.setText(user.getName());
                mAdapter.clear();
                for ( User user : users){
                    mAdapter.addUser(user);
                }
                }else{
                    Toast.makeText(MainActivity.this,"No followers",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        usersStack.pop();
        if (! usersStack.isEmpty()){
            setCurrentUser(usersStack.pop());
        }
        else{
            finish();
        }
    }
}
