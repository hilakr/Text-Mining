package com.hilareut.todoapp.activities;


import android.app.DialogFragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hilareut.todoapp.DAO.DAO;
import com.hilareut.todoapp.DAO.DBHelper;
import com.hilareut.todoapp.DAO.ParseTask;
import com.hilareut.todoapp.fragments.DatePickerFragment;
import com.hilareut.todoapp.R;
import com.hilareut.todoapp.common.Task;
import com.hilareut.todoapp.fragments.TimePickerFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import com.parse.ParseUser;
import com.parse.SaveCallback;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DisplayTaskActivity extends  ActionBarActivity implements AdapterView.OnItemSelectedListener {


    private static final int ACTION_TAKE_PHOTO_B = 1;
    private static final int ACTION_TAKE_PHOTO_S = 2;
    private static final String BITMAP_STORAGE_KEY = "viewbitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;


    private DBHelper mydb ;
    private Task task = new Task(); //local task instance


    TextView tTaskName ;
    Spinner tCategory;
    TextView tLocation;
    RadioGroup tPriority;
    Spinner tAssignedTo;
    EditText tDate;
    EditText tTime;
    RadioGroup tStatus;
    boolean is_manager;
    ImageView mImageView;
    Bitmap mImageBitmap;
    Button bTakePic;
    DAO dao;

    Button.OnClickListener mTakePicSOnClickListener =
            new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{

            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_display_task);

            //Check if Current user is manager or employee
            is_manager = ParseUser.getCurrentUser().getBoolean("isManager");

            Log.e("is_manager_in_task", Boolean.toString(is_manager));

            //init local task instance
            task.setHasDate(false);
            tDate = (EditText) findViewById(R.id.taskDateEdit);
            tTime = (EditText) findViewById(R.id.taskTimeEdit);
            tDate.setInputType(InputType.TYPE_NULL);
            tTime.setInputType(InputType.TYPE_NULL);

            //add listener to pop up date picker
            tDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) showDatePickerDialog(v);
                }
            });

            //add listener to pop up time picker
            tTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) showTimePickerDialog(v);
                }
            });


            tTaskName = (TextView) findViewById(R.id.editTaskName);

            tCategory = (Spinner) findViewById(R.id.categoriesSpinner);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapterCategorySpinner = ArrayAdapter.createFromResource(this,
                    R.array.categories_arrays, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapterCategorySpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            tCategory.setAdapter(adapterCategorySpinner);
            tCategory.setOnItemSelectedListener(this);

            tLocation = (TextView) findViewById(R.id.editLocation);

            tPriority = (RadioGroup) findViewById(R.id.priorityRadioButton);

            tAssignedTo = (Spinner) findViewById(R.id.assignedSpinners);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapterAssignetoSpinner = ArrayAdapter.createFromResource(this,
                    R.array.categories_arrays, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapterAssignetoSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner

            createAssignedList(tAssignedTo);

            tStatus = (RadioGroup) findViewById(R.id.statusRadioButton);

           /* Image */
            mImageView = (ImageView) findViewById(R.id.imageView1);
            mImageBitmap = null;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
            } else {
                mAlbumStorageDirFactory = new BaseAlbumDirFactory();
            }


            bTakePic = (Button) findViewById(R.id.bTakePic);
            setBtnListenerOrDisable(
                    bTakePic,
                    mTakePicSOnClickListener,
                    MediaStore.ACTION_IMAGE_CAPTURE
            );


            mydb = new DBHelper(this);

            dao = DAO.getInstance(getApplicationContext());

            String taskId = getIntent().getStringExtra("id");
            if (taskId != null) {
                //means this is the view part not the add task part.
                Log.i("get_data", taskId);
                Cursor rs = mydb.getData(taskId);
                if (rs == null) {
                    Log.e("get_data", "failed");
                    return;
                }

                String taskName = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TASK_NAME));
                String category = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TASK_CATEGORY));
                String location = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TASK_LOCATION));
                String dueDate = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TASK_DUE_DATE));
                String priority = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TASK_PRIORITY));
                String assignedTo = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TASK_ASSIGNED_TO));
                String accept = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TASK_ACCEPT));
                String status = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TASK_STATUS));
                byte[] image = rs.getBlob(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_TASK_IMAGE));
                if (!rs.isClosed()) {
                    rs.close();
                }

                tTaskName.setText((CharSequence) taskName);
                tTaskName.setFocusable(false);
                tTaskName.setClickable(false);


                int index = getIndexForSpinner(tCategory, category);
                tCategory.setSelection(index);
                tCategory.setFocusable(false);
                tCategory.setClickable(false);
                tCategory.setEnabled(false);

                tLocation.setText((CharSequence) location);
                tLocation.setFocusable(false);
                tLocation.setClickable(false);

                ((RadioButton) tPriority.getChildAt(Integer.valueOf(priority))).setChecked(true);
                tPriority.setEnabled(false);
                tPriority.setFocusable(false);
                tPriority.setClickable(false);
                for (int i = 0; i < tPriority.getChildCount(); i++) {
                    tPriority.getChildAt(i).setClickable(false);
                }

                try {

                    String dateString = dueDate;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
                    Date convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse(dateString);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println(convertedDate);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(convertedDate);
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
                    tDate.setText(strDate);
                    tDate.setFocusable(false);
                    tDate.setClickable(false);
                    tTime.setText(strTime);
                    tTime.setFocusable(false);
                    tTime.setClickable(false);
                    task.setHasDate(false);
                } catch (Exception e) {
                    task.setHasDate(false);
                };

                index = getIndexForSpinner(tAssignedTo, assignedTo);
                tAssignedTo.setSelection(index);
                tAssignedTo.setFocusable(false);
                tAssignedTo.setClickable(false);
                tAssignedTo.setEnabled(false);

                if (status.compareToIgnoreCase("done")!=0 )
                {
                    bTakePic.setVisibility(View.GONE);
                    mImageView.setVisibility(View.GONE);
                }
                index = getIndexForRadioGroup(tStatus, status);
                ((RadioButton) tStatus.getChildAt(index)).setChecked(true);
                tStatus.setFocusable(false);
                tStatus.setClickable(false);
                for (int i = 0; i < tStatus.getChildCount(); i++) {
                    tStatus.getChildAt(i).setClickable(false);
                }

                if (status.compareToIgnoreCase("done") == 0)
                {
                    bTakePic.setVisibility(View.VISIBLE);
                    if (image != null)
                    {
                        mImageView.setImageBitmap(createImageFromBytes(image));
                        mImageView.setVisibility(View.VISIBLE);
                    }

                }

            }

        }
        finally {
           if (mydb!=null)
            mydb.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        String taskId = getIntent().getStringExtra("id");

        if(taskId !=null) {
                getMenuInflater().inflate(R.menu.display_task, menu);
            }


        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        super.onOptionsItemSelected(item);
        switch(item.getItemId())
        {
            case R.id.Edit_Contact:

                if (is_manager)
                {

                    Button b = (Button)findViewById(R.id.save_task_button);
                    b.setVisibility(View.VISIBLE);

                    tTaskName.setEnabled(true);
                    tTaskName.setFocusableInTouchMode(true);
                    tTaskName.setClickable(true);

                    tCategory.setEnabled(true);
                    tCategory.setFocusableInTouchMode(true);
                    tCategory.setClickable(true);

                    tLocation.setEnabled(true);
                    tLocation.setFocusableInTouchMode(true);
                    tLocation.setClickable(true);

                    tDate.setEnabled(true);
                    tDate.setFocusableInTouchMode(true);
                    tDate.setClickable(true);

                    tTime.setEnabled(true);
                    tTime.setFocusableInTouchMode(true);
                    tTime.setClickable(true);

                    tPriority.setEnabled(true);
                    tPriority.setFocusableInTouchMode(true);
                    tPriority.setClickable(true);
                    for (int i = 0; i < tPriority.getChildCount(); i++) {
                        tPriority.getChildAt(i).setClickable(true);
                    }

                    tAssignedTo.setEnabled(true);
                    tAssignedTo.setFocusableInTouchMode(true);
                    tAssignedTo.setClickable(true);

                } else
                {

                    Button b = (Button) findViewById(R.id.save_task_button);
                    b.setVisibility(View.VISIBLE);

                    LinearLayout employee_layouy = (LinearLayout)findViewById(R.id.edit_employee_layout);
                    Drawable d = getDrawable(R.drawable.border);
                    employee_layouy.setBackground(d);
                    tTaskName.setFocusableInTouchMode(false);
                    tTaskName.setClickable(false);

                    tCategory.setEnabled(false);
                    tCategory.setFocusableInTouchMode(false);
                    tCategory.setClickable(false);


                    tLocation.setFocusableInTouchMode(false);
                    tLocation.setClickable(true);


                    tDate.setFocusableInTouchMode(false);
                    tDate.setClickable(false);


                    tTime.setFocusableInTouchMode(false);
                    tTime.setClickable(false);

                    tPriority.setEnabled(true);
                    tPriority.setFocusableInTouchMode(true);
                    tPriority.setClickable(true);
                    for (int i = 0; i < tPriority.getChildCount(); i++) {
                        tPriority.getChildAt(i).setClickable(false);
                    }

                    tAssignedTo.setFocusableInTouchMode(false);
                    tAssignedTo.setClickable(false);
                    tAssignedTo.setEnabled(false);

                    tStatus.setEnabled(true);
                    tStatus.setFocusableInTouchMode(true);
                    tStatus.setClickable(true);
                    for (int i = 0; i < tStatus.getChildCount(); i++) {
                        tStatus.getChildAt(i).setClickable(true);
                    }


                }
             return true;
//            case R.id.Delete_Contact:
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setMessage(R.string.deleteContact)
//                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//
//                                mydb.deleteTask(id_To_Update);
//                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // User cancelled the dialog
//                            }
//                        });
//                AlertDialog d = builder.create();
//                d.setTitle("Are you sure");
//                d.show();
//
//                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void run(View view)
    {

            String taskId = getIntent().getStringExtra("id");

            final Task task = new Task();
            task.setId_task(taskId);
            task.setDescription(tTaskName.getText().toString());
            task.setCategory(tCategory.getSelectedItem().toString());
            task.setLocation(tLocation.getText().toString());

            RadioGroup radioButtonGroup = (RadioGroup)findViewById(R.id.priorityRadioButton);
            int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
            View radioButton = radioButtonGroup.findViewById(radioButtonID);
            int idx = radioButtonGroup.indexOfChild(radioButton);
            task.setPriority(idx);


            String oldstring = tDate.getText()+" "+tTime.getText();
            try
            {
                Date myDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                myDate =  sdf.parse(oldstring);
                task.setDueDate(myDate);
                task.setHasDate(true);
            }
            catch(Exception e)
            {
                task.setHasDate(false);
            }

            task.setAssignedTo(tAssignedTo.getSelectedItem().toString());


            radioButtonGroup = (RadioGroup)findViewById(R.id.statusRadioButton);
            radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
            radioButton = radioButtonGroup.findViewById(radioButtonID);
            idx = radioButtonGroup.indexOfChild(radioButton);
            switch (idx)
            {
                case 0: task.setStatus("waiting");
                    break;
                case 1: task.setStatus("InProcess");
                    break;
                case 2: task.setStatus("done");
                    break;
                default:
                    task.setStatus("waiting");
            }
            try{

                task.setImage(createBytesFromImage(mImageBitmap));
            }
            catch (Exception e){
                task.setImage(null);

            }
            if(taskId != null) {
                //UPDATE TASK
                ParseQuery<ParseTask> query = new ParseQuery<ParseTask>(ParseTask.class);
                query.getInBackground(task.getId_task(), new GetCallback<ParseTask>() {
                    @Override
                    public void done(ParseTask parseTask, ParseException e) {
                        if (e == null) {
                            parseTask.updateData(task);
                            parseTask.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (mydb.updateTask(task)) {
                                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
            else {
                //INSERT TASK
                final ParseTask parseTask = new ParseTask();
                parseTask.updateData(task);
                parseTask.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("save_task", parseTask.getObjectId());
                            task.setId_task(parseTask.getObjectId());
                            task.setStatus("waiting");
                            //0 = waiting
                            task.setAccept(0);
                            if (mydb.insertTask(task)) {
                                Toast.makeText(getApplicationContext(), "New Task created and sent", Toast.LENGTH_SHORT).show();
                                DisplayTaskActivity.this.finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_SHORT).show();
                            }
//
//                            Intent myIntent = new Intent(getBaseContext(), ReminderNotification.class);
//                            myIntent.putExtra("task", task);
//
//                            PendingIntent pendingIntent =
//                                    PendingIntent.getBroadcast(getBaseContext(), Integer.valueOf(task.getId_task()) , myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//                            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//
//                            Calendar calendar = Calendar.getInstance();
//
//                            calendar.setTimeInMillis(task.getDueDate().getTime());
//
//                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                        } else {
                            Log.e("save_task", e.getMessage());
                        }
                    }
                });
            }
    }

    //private method of your class
    private int getIndexForSpinner(Spinner spinner, String myString)
    {
        int index = 0;
        for (int i = 0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    //private method of your class
    private int getIndexForRadioGroup(RadioGroup radioGroup, String myString) {
        int index = 0;

        for (int i=0;i<radioGroup.getChildCount();i++){
           String str = radioGroup.getChildAt(i).toString();
            if (radioGroup.getChildAt(i).toString().contains(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.normalRadioButton:
                if (checked)
                    task.setPriority(0);
                    break;
            case R.id.lowRadioButton:
                if (checked)
                    // Ninjas rule
                task.setPriority(1);
                    break;
            case R.id.highRadioButton:
                if (checked)
                    // Ninjas rule
                    task.setPriority(2);
                    break;
            case R.id.doneStatusButton:
                task.setStatus("done");
                bTakePic.setVisibility(View.VISIBLE);
                Log.e("start", "camera");
                break;
            case R.id.waitingStatusbutton:
                task.setStatus("waiting");
                break;
            case R.id.InProcessStatusButton:
                task.setStatus("InProcess");
                break;
        }


    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");


    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");

    }

    public void changeOption(RadioGroup radiogroup) {
        if (radiogroup.isEnabled()) {
            radiogroup.setEnabled(false);
        } else {
            radiogroup.setEnabled(true);

        }
    }

    public void createAssignedList(Spinner tAssignedTo) {
        ArrayList users = new ArrayList();
        DBHelper mydb = new DBHelper(this);
        users = mydb.getAllUsersNames();
       // Collections.sort(users);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, users);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tAssignedTo.setAdapter(dataAdapter);
        tAssignedTo.setOnItemSelectedListener(this);
        //tAssignedTo.setSelection(1);

    }

    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }


    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

		/* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

		/* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);
        mImageView.setVisibility(View.VISIBLE);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch(actionCode) {
            case ACTION_TAKE_PHOTO_B:
                File f = null;

                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }
                break;

            default:
                break;
        } // switch

        startActivityForResult(takePictureIntent, actionCode);
    }

    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);
        mImageView.setVisibility(View.VISIBLE);

    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {
            setPic();
            galleryAddPic();
            mCurrentPhotoPath = null;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B: {
                if (resultCode == RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            } // ACTION_TAKE_PHOTO_B

            case ACTION_TAKE_PHOTO_S: {
                if (resultCode == RESULT_OK) {
                    handleSmallCameraPhoto(data);
                }
                break;
            } // ACTION_TAKE_PHOTO_S


        } // switch
    }




    public  byte[] createBytesFromImage(Bitmap imageBitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public  Bitmap createImageFromBytes(byte[] imageBytes){
        if (imageBytes == null){
            return BitmapFactory.decodeByteArray(imageBytes, 0, 0);

        }
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }


    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    private void setBtnListenerOrDisable(
            Button btn,
            Button.OnClickListener onClickListener,
            String intentName
    ) {
        if (isIntentAvailable(this, intentName)) {
            btn.setOnClickListener(onClickListener);
        } else {
            btn.setText(
                    getText(R.string.cannot).toString() + " " + btn.getText());
            btn.setClickable(false);
        }
    }




}