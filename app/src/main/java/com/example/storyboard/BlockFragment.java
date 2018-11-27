package com.example.storyboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camera.R;
import com.example.database.Composition;
import com.example.database.DBHelper;
import com.example.database.Story;
import com.example.database.Table;
import com.example.dialog.BaseCallbackDialog;
import com.example.dialog.YesNoDialogFragment;

import org.w3c.dom.Text;

import static com.example.database.DBHelper.URL;
import static com.example.dialog.BaseCallbackDialog.ARG_LISTENER_TYPE;

public class BlockFragment extends Fragment
        implements YesNoDialogFragment.DialogCallbackListener {
    private DBHelper dbHelper;
    private int storyBoardNumber;
    private int blockID;
    private View rootView;

    public static BlockFragment newInstance(DBHelper dbHelper, int storyBoardNumber, int blockID){
        BlockFragment fragment = new BlockFragment();
        fragment.dbHelper = dbHelper;
        fragment.storyBoardNumber = storyBoardNumber;
        fragment.blockID = blockID;
        return fragment;
    }

    public BlockFragment(){

    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.block_main, container, false);
        return rootView;
    }

/*
@Override
public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
}
*/

    // /*
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String record [] = dbHelper.getRecord(Table.STORY, String.valueOf(blockID));
        final int compositionID = Integer.parseInt(dbHelper.getColumn(
                Table.COMPOSITION, Composition.THUMB_ID.getName(),
                Composition.ID.getName(), record[Story.COMPOSITION_ID.getNumber()]
        )[0]);


        Button modify = (Button) view.findViewById(R.id.modify);
        Button delete = (Button) view.findViewById(R.id.delete);
        final TextView name = (TextView) view.findViewById(R.id.name);
        final TextView desc = (TextView) view.findViewById(R.id.description);
        final ImageView layout = (ImageView) view.findViewById(R.id.composition);

        name.setText(record[Story.NAME.getNumber()]);
        desc.setText(record[Story.DESCRIPTION.getNumber()]);
        layout.setImageResource(compositionID);

        modify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();

                if (fragmentManager != null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // BackStackを設定
                    fragmentTransaction.addToBackStack(null);

                    DetailSettingFragment detailSettingFragment
                            = DetailSettingFragment.newInstance(
                                    dbHelper, storyBoardNumber,
                            Integer.parseInt(record[Story.COMPOSITION_ID.getNumber()]),
                            Integer.parseInt(record[Story.ID.getNumber()])
                    );

                    fragmentTransaction.replace(R.id.container, detailSettingFragment);
                    fragmentTransaction.commit();

                }
            }
        });


        final YesNoDialogFragment dialog
                = YesNoDialogFragment.newInstance(dbHelper, Table.STORY, blockID);
        dialog.setCallbackListener(this);

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.show(getFragmentManager(), "");
            }
        });
    }
    // */

    @Override
    public void onPositiveButtonClicked() {
        dbHelper.deleteStoryRecord(String.valueOf(blockID), String.valueOf(storyBoardNumber));
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }

}
