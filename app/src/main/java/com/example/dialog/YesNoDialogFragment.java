package com.example.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

import com.example.database.DBHelper;
import com.example.database.Story;
import com.example.database.Table;

public class YesNoDialogFragment
        extends BaseCallbackDialog<YesNoDialogFragment.DialogCallbackListener> {

    private DBHelper dbHelper;
    private Table table;
    private int primaryKey;
    private boolean delete;

    public interface DialogCallbackListener {
        void onPositiveButtonClicked();
        void onNegativeButtonClicked();
    }

    public static YesNoDialogFragment
    newInstance(DBHelper dbHelper, Table table, int primaryKey){
        YesNoDialogFragment fragment = new YesNoDialogFragment();
        fragment.dbHelper = dbHelper;
        fragment.table = table;
        fragment.primaryKey = primaryKey;
        fragment.delete = false;
        return fragment;
    }

    public YesNoDialogFragment(){

    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String name = dbHelper.getColumn(
                table, "name", "id", String.valueOf(primaryKey)
        )[0];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("delete");
        builder.setMessage("Are you sure you want to delete" + " \"" + name + "\"");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getCallbackListener() != null) {
                    getCallbackListener().onPositiveButtonClicked();
                }
            }
        });
        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getCallbackListener() != null) getCallbackListener().onNegativeButtonClicked();
            }
        });
        Dialog dialog = builder.create();
        return dialog;
    }
}
