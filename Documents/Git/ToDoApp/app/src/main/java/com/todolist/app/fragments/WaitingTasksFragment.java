package com.todolist.app.fragments;

/**
 * Created by Hila on 3/1/16.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.todolist.app.DAO.DAO;
import com.todolist.app.DAO.DBHelper;
import com.todolist.app.activities.DisplayTaskActivity;
import com.todolist.app.activities.MainActivity;
import com.todolist.app.common.User;
import com.todolist.app.listeners.ItemClickListener;
import com.todolist.app.listeners.ItemLongClickListener;
import com.todolist.app.R;
import com.todolist.app.bl.TaskListFragmentAdapter;
import com.parse.ParseUser;

import java.util.List;

public class WaitingTasksFragment extends Fragment implements ItemLongClickListener,
        ItemClickListener,SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public TaskListFragmentAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        // Give the TabLayout the ViewPager

        mRecyclerView.setLayoutManager(mLayoutManager);

        MainActivity activity = (MainActivity)getActivity();

        activity.scheduleAlarm();

        mRecyclerView.setAdapter(activity.mWaitingTasksAdapter);
        mAdapter = activity.mWaitingTasksAdapter;

        //number of tasks
        TextView view = (TextView) rootView.findViewById(R.id.numberOfWaitingTask);
        view.setText(Integer.toString(activity.mWaitingTasksAdapter.getItemCount()));

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();
        try{
//            mydb =  new DBHelper(getContext());
//            ArrayList tasks = mydb.getWaitingTasks(mydb.convertParseUsertoUser(ParseUser.getCurrentUser()));
//            mAdapter.updateList(tasks);
            mAdapter.notifyDataSetChanged();

        }finally {
            //mydb.close();
            System.out.print("bla");
        }
    }


    @Override
    public void onClick(View view, int pos, boolean flag) {
        Button save = (Button) getActivity().findViewById(R.id.save_task_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.mWaitingTasksAdapter.notifyDataSetChanged();
                Log.d("sort", "refresh alarm is clicked fragment");


            }
        });

    }

    @Override
    public void onItemLongClick(View view, int pos) {
        Log.d("sort", "refresh alarm is clicked fragment");


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        MainActivity activity = (MainActivity)getActivity();
        List sortedList;
        switch(item.getItemId())
        {
            case R.id.addNewTask:
                Intent intent = new Intent(getContext(),DisplayTaskActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_sort:
                return true;
            case R.id.sortByDueTime:
                List list = mAdapter.getUpdateListTasks();
                sortedList =  activity.sortList(mAdapter.getUpdateListTasks(),1);
                mAdapter.updateList(sortedList);
                mAdapter.notifyDataSetChanged();
                Log.d("sort", "sortbytime is clicked in fragment");
                return true;
            case R.id.sortByPriority:
                sortedList =  activity.sortList(mAdapter.getUpdateListTasks(),2);
                mAdapter.updateList(sortedList);
                mAdapter.notifyDataSetChanged();
                Log.d("sort","sortbypriority is clicked fragment");
                return true;
            case R.id.sortByStatus:
                sortedList =  activity.sortList(mAdapter.getUpdateListTasks(),3);
                mAdapter.updateList(sortedList);
                mAdapter.notifyDataSetChanged();
                Log.d("sort","sortbywaitingstatus is clicked fragment");
                return true;
            case R.id.checkForNewTasks:
                DAO dao = DAO.getInstance(getContext());
                DBHelper mydb = new DBHelper(getContext());
                dao.loadFromParse();
                User user = mydb.convertParseUsertoUser(ParseUser.getCurrentUser());
                List updatedlist = mydb.getWaitingTasks(user);
                mAdapter.updateList(updatedlist);
                mAdapter.notifyDataSetChanged();
                //number of tasks
                TextView view = (TextView) getView().findViewById(R.id.numberOfWaitingTask);
                view.setText(Integer.toString(mAdapter.getItemCount()));
                Log.d("sort","refresh alarm is clicked fragment");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onRefresh() {
        DAO dao = DAO.getInstance(getContext());
        DBHelper mydb = new DBHelper(getContext());
        dao.loadFromParse();
        User user = mydb.convertParseUsertoUser(ParseUser.getCurrentUser());
        List updatedlist = mydb.getWaitingTasks(user);
        mAdapter.updateList(updatedlist);
        mAdapter.notifyDataSetChanged();
        //number of tasks
        TextView view = (TextView) getView().findViewById(R.id.numberOfWaitingTask);
        view.setText(Integer.toString(mAdapter.getItemCount()));
        mSwipeRefreshLayout.setRefreshing(false);
        Log.d("sort", "refresh alarm is clicked fragment");

    }




}




