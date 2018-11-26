package com.example.storyboard;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.example.database.DBHelper;
import com.example.database.Table;

import java.util.ArrayList;
import java.util.List;

public class BlockDialogFragment extends DialogFragment {

    private DBHelper dbHelper;
    private Table table;

    public static BlockDialogFragment newInstance(DBHelper dbHelper){
        BlockDialogFragment fragment = new BlockDialogFragment();
        fragment.dbHelper = dbHelper;
        return fragment;
    }

    public BlockDialogFragment(){

    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items = {"rename", "delete"};
        /*
        int defaultItem = 0; // デフォルトでチェックされているアイテム
        final List<Integer> checkedItems = new ArrayList<>();
        checkedItems.add(defaultItem);
        */
        return new AlertDialog.Builder(getActivity())
                .setTitle("Selector")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item_which pressed
                    }
                })
                /*
                .setSingleChoiceItems(items, defaultItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkedItems.clear();
                        checkedItems.add(which);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!checkedItems.isEmpty()) {
                            Log.d("checkedItem:", "" + checkedItems.get(0));
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                */
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();

        // onPause でダイアログを閉じる場合
        dismiss();
    }
}
