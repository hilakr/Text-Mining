package com.todolist.app.bl;

/**
 * Created by Hila on 3/3/16.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.todolist.app.R;
import com.todolist.app.common.User;
import com.todolist.app.listeners.ItemClickListener;
import com.todolist.app.listeners.ItemLongClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by moshe on 19-01-16.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context context;
    private List<User> users;
    private ItemClickListener mItemClickListener;
    private ItemLongClickListener mItemLongClickListener;


    public UserListAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;

    }

    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_user_view_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v , mItemClickListener , mItemLongClickListener);

        return viewHolder;
    }



    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        //Each item is a view in the card.
        private TextView mUserName;
        private TextView mUserEmail;
        private TextView mUserPhone;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView, ItemClickListener mItemClickListener,
                          ItemLongClickListener mItemLongClickListener) {
            super(itemView);
            mUserName = (TextView) itemView.findViewById(R.id.textView_username);
            mUserEmail = (TextView) itemView.findViewById(R.id.textView_useremail);
            mUserPhone = (TextView)itemView.findViewById(R.id.textView_userphone);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), false);

        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }

    @Override
    public void onBindViewHolder(UserListAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.mUserName.setText(user.getUser_name());
        holder.mUserEmail.setText(user.getUser_email());
        holder.mUserPhone.setText(user.getUser_phone());


        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                User user = users.get(position);

            }


        });
    }


    public void updateList(ArrayList<User> data) {
        this.users = data;
        notifyDataSetChanged();
    }

    public List<User> getUpdateListTasks(){

        return this.users;
    }



}