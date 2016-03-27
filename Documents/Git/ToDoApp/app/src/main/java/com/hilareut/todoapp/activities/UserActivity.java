package com.hilareut.todoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hilareut.todoapp.DAO.DBHelper;
import com.hilareut.todoapp.R;
import com.hilareut.todoapp.bl.UserListAdapter;
import com.hilareut.todoapp.dialogs.AboutDialog;
import com.hilareut.todoapp.dialogs.StudentsDialog;
import com.hilareut.todoapp.listeners.ItemClickListener;
import com.hilareut.todoapp.listeners.ItemLongClickListener;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Hila on 3/13/16.
 */
public class UserActivity extends AppCompatActivity implements ItemLongClickListener,
        ItemClickListener
{

    private RecyclerView mRecyclerView;
    private android.support.v7.widget.RecyclerView.LayoutManager mLayoutManager;
    public UserListAdapter mAdapter;
    DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_layout);
        Log.e("userfragment","created");


        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_user);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        // Give the TabLayout the ViewPager

        mRecyclerView.setLayoutManager(mLayoutManager);
        dbHelper = new DBHelper(getApplicationContext());
        List list = dbHelper.getAllUsers();
        mAdapter = new UserListAdapter(dbHelper.getAllUsers(),getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        TextView view = (TextView) findViewById(R.id.numOfUsers);
        view.setText(Integer.toString(mAdapter.getItemCount()));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(),CreateATeamActivity.class);
                startActivity(intent2);
                mAdapter.notifyDataSetChanged();
                finish();
            }
        });
    }



    public void setRecyclerViewLayoutManager(RecyclerView.LayoutManager layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }


    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view, int pos, boolean flag) {
        Button save = (Button) findViewById(R.id.save_task_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onItemLongClick(View view, int pos) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_team, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.mangeTask:
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent2);
                return true;
            case R.id.app_name:
                AboutDialog about = new AboutDialog();
                about.show(getSupportFragmentManager(), "About");
                return true;
            case R.id.owners:
                StudentsDialog dialog = new StudentsDialog();
                dialog.show(getSupportFragmentManager(), "Students");
                return true;
            case R.id.logout:
                ParseUser.logOut();
                Intent intent3 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
