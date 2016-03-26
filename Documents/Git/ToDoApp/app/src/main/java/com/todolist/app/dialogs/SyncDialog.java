package com.todolist.app.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.todolist.app.R;

public class SyncDialog extends DialogFragment {
    private static EditText eSync;
    public String sync;
    public NoticeDialogListener result;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.change_sync_time, null))
                // Add action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        eSync = (EditText)getDialog().findViewById(R.id.editText_change_sync_time);
                        sync = eSync.getText().toString();
                        if( result != null ){
                             result.finish(String.valueOf(eSync.getText()));
                        }
                        SyncDialog.this.dismiss();
                    }


                });

        return builder.create();
    }
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
//        public void onDialogPositiveClick(DialogFragment dialog);
//        public void onDialogNegativeClick(DialogFragment dialog);
        public void finish (String result);
    }

    public void setDialogResult(NoticeDialogListener dialogResult){
        result = dialogResult;
    }
    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;




}