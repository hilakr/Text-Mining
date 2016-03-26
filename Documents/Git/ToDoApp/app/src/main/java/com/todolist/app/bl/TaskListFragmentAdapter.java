package com.todolist.app.bl;

/**
 * Created by Hila on 3/3/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.todolist.app.DAO.DAO;
import com.todolist.app.R;
import com.todolist.app.activities.DisplayTaskActivity;
import com.todolist.app.common.Task;
import com.todolist.app.listeners.ItemClickListener;
import com.todolist.app.listeners.ItemLongClickListener;
import com.parse.ParseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by moshe on 19-01-16.
 */
public class TaskListFragmentAdapter extends RecyclerView.Adapter<TaskListFragmentAdapter.ViewHolder>  {

        private Context context;
        private  List tasks;
        private ItemClickListener mItemClickListener;
        private ItemLongClickListener mItemLongClickListener;
        private DAO dao;

        public TaskListFragmentAdapter( Context context,List<Task> taskList) {
            this.context = context;
            this.tasks = taskList;
            this.dao = DAO.getInstance(context);

        }

    @Override
        public TaskListFragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(context)
                    .inflate(R.layout.list_view_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(v , mItemClickListener , mItemLongClickListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final TaskListFragmentAdapter.ViewHolder holder, final int position) {
           final Task task = (Task)tasks.get(position);
            holder.mTvTaskName.setText(task.getDescription());
            switch (task.getPriority()){
                case 0:
                    holder.mTvPriority.setText("low");
                    break;
                case 1:
                    holder.mTvPriority.setText("normal");
                    break;
                case 2:
                    holder.mTvPriority.setText("high");
                    break;
                case 3:
                    holder.mTvPriority.setText("high");
                    break;
                default:
                    holder.mTvPriority.setText("normal");
            }
            if (ParseUser.getCurrentUser().get("isManager")== 0)
            {
                holder.mTvStatus.setText(task.getAssignedTo());

            }
            else
            {
                holder.mTvStatus.setText(task.getStatus());
            }

            try {
                Date myDate = task.getDueDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(myDate);
                int day = calendar.get(Calendar.DAY_OF_MONTH); //Day of the month :)
                int minutes = calendar.get(Calendar.MINUTE); //number of seconds
                int month = calendar.get(Calendar.MONTH) + 1;
                int year = calendar.get(Calendar.YEAR);
                int hours = calendar.get(Calendar.HOUR);
                String am_pm = null;
                if (calendar.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "AM";
                else if (calendar.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "PM";
                if (am_pm.compareTo("PM") == 0) {
                    hours = hours + 12;
                }
                String strDate = day + "/" + month + "/" + year;
                String strTime = hours + ":" + minutes;
                String str = strDate + " " + strTime;

                holder.mTvDueDate.setText(str);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (task.getAccept()!=0){
                holder.acceptToggle.setChecked(true);
                holder.acceptToggle.setEnabled(false);
            }

            else
            {
                holder.acceptToggle.setChecked(false);
            }


            holder.acceptToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Log.e("toggle", "accept");
                        task.setAccept(2);
                        dao.updateTask(task);
                        buttonView.setChecked(true);
                        buttonView.setEnabled(false);
                        Toast.makeText(context, "Task is Accepted" + " Successfully", Toast.LENGTH_SHORT).show();
                        Log.e("position", Integer.toString(position));

                    } else {
                        // The toggle is disabled
                        Log.e("toggle","pending");
                        buttonView.setChecked(false);
                        Log.e("position", Integer.toString(position));
                    }
                }
            });

            holder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    Log.e("toggle", Integer.toString(((Task) tasks.get(position)).getAccept()));
                    Intent intent = new Intent(context, DisplayTaskActivity.class);
                    intent.putExtra("id", task.getId_task());
                    ((Activity) context).startActivity(intent);



                  /*  if (loginController.isAdmin()) {
                        Intent intent = new Intent(context, EditTaskActivity.class);
                        intent.putExtra(Constants.EDIT_TASK_ID, task.getId());
                        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE_TASK);
                    } else {
                        Intent intent = new Intent(context, ReportTaskActivity.class);
                        intent.putExtra(Constants.EDIT_TASK_ID, task.getId());
                        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_CODE_UPDATE_TASK);
                    }*/
                }


            });
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
            //Each item is a view in the card.
            private TextView mTvTaskName;
            private TextView mTvDueDate;
            private TextView mTvPriority;
            private TextView mTvStatus;
            private ToggleButton acceptToggle;
            private ItemClickListener clickListener;

            public ViewHolder(View itemView, ItemClickListener mItemClickListener,
                              ItemLongClickListener mItemLongClickListener) {
                super(itemView);
                mTvTaskName = (TextView) itemView.findViewById(R.id.textView_description);
                mTvDueDate = (TextView) itemView.findViewById(R.id.textView_dueTime);
                mTvPriority = (TextView)itemView.findViewById(R.id.textView_priority);
                mTvStatus = (TextView)itemView.findViewById(R.id.textView_status);
                acceptToggle = (ToggleButton)itemView.findViewById(R.id.acceptToggleButton);
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


        public void updateList(List<Task> data) {
            tasks = data;
            notifyDataSetChanged();
        }

        public List<Task> getUpdateListTasks(){
            return this.tasks;
        }

}