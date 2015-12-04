package com.bypassmobile.octo.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bypassmobile.octo.R;
import com.bypassmobile.octo.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nejasix on 12/1/15.
 */
public class GithubUserAdapter extends RecyclerView.Adapter<GithubUserAdapter.ViewHolder> {

    private Context mContext;

    private List<User> userList;

    private OnUserClicked callback;

    public GithubUserAdapter(Context context , OnUserClicked callback)
    {
        mContext = context;
        userList = new ArrayList<User>();
        this.callback = callback;
    }

    public void addUser(User user)
    {
        userList.add(user);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User user = userList.get(position);
        Picasso.with(mContext).load(user.getProfileURL()).resize(90,90).centerCrop().into(holder.userImage);
        holder.userNameTextField.setText(user.getName());
        holder.currentUser = user;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void clear() {
        userList.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.user_name)
        TextView userNameTextField;

        @Bind(R.id.user_image)
        ImageView userImage;

        public User currentUser;

        @OnClick(R.id.card_view)
        public void userClicked()
        {
            callback.setUser(currentUser);
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
