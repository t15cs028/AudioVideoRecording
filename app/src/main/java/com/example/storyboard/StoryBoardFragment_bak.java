package com.example.storyboard;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.camera.CameraFragment;
import com.example.camera.R;
import com.example.database.DBHelper;
import com.example.database.Stories;
import com.example.database.Story;
import com.example.database.Table;

import java.util.ArrayList;


/*****************/
public class StoryBoardFragment_bak extends Fragment /*implements BlockListAdapter.BlockListAdapterListener*/{

    private DBHelper dbHelper;
    private int storyBoardNumber;
    private int blocks;
    private View rootView;

    public StoryBoardFragment_bak() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.storyboard_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        int count = dbHelper.getNumOfRecord(Table.STORIES);

        String name[]
                = dbHelper.getColumn(
                        Table.STORY, Story.NAME.getName(),
                        Story.STORIES_ID.getName(), String.valueOf(storyBoardNumber));

        String layout[]
                = dbHelper.getColumn(
                        Table.STORY, Story.COMPOSITION_ID.getName(),
                        Story.STORIES_ID.getName(), String.valueOf(storyBoardNumber));

        ArrayList<Block> listItem = new ArrayList<>();
        ListView list = (ListView) rootView.findViewById(R.id.sample_listview);

        Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher_round);

        Block block = new Block(0, "To create a new block", bmp, null);
        listItem.add(block);

        if (name != null){
            for(int i = 0; i < name.length; i++) {
                int comp_id = Integer.parseInt(layout[i]);
                bmp = BitmapFactory.decodeResource(getResources(),
                        comp_id);

                Bitmap record = BitmapFactory.decodeResource(getResources(),
                        android.R.drawable.ic_menu_camera);
                block = new Block(i+1, name[i], bmp, record);
                listItem.add(block);
            }
        }
        /*****************/
        /*
        BlockListAdapter adapter =
                new BlockListAdapter(getActivity(), R.layout.listblocks_main, listItem);

        list.setAdapter(adapter);
        list.setOnItemClickListener(onItemClickListener);
        list.setOnItemLongClickListener(onItemLongClickListener);
        adapter.setListener(this);
        */
        /*****************/

    }

    // リストの普通のクリックをしたときの動作
    private AdapterView.OnItemClickListener onItemClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            // タップしたアイテムの取得
            ListView listView = (ListView)parent;
            Block item = (Block)listView.getItemAtPosition(position);  // SampleListItemにキャスト


            // Log.d(TAG, "onListItemClick position => " + position + " : id => " + id);

            // タッチされた絵コンテ作品のIDを取得
            String storiesID[] = dbHelper.getColumn(Table.STORIES, Stories.ID.getName());


            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // BackStackを設定
                fragmentTransaction.addToBackStack(null);
                if(position == 0){
                    CompositionFragment compositionFragment = new CompositionFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("DBHelper", dbHelper);
                    args.putInt("StoryBoardNumber", storyBoardNumber);
                    compositionFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.container, compositionFragment);
                }
                else {
                    /*
                    fragmentTransaction.replace(R.id.container,
                            new StoryBoardFragment(dbHelper, Integer.parseInt(storiesID[position-1])));
                    */
                }
                fragmentTransaction.commit();
            }
        }
    };

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == 100) {
            if (resultCode == DialogInterface.BUTTON_POSITIVE) {
                // positive_button 押下時の処理
            } else if (resultCode == DialogInterface.BUTTON_NEGATIVE) {
                // negative_button 押下時の処理
            }
        }
    }


    // リストをロングクリックしたときの動作
    private AdapterView.OnItemLongClickListener onItemLongClickListener
            = new AdapterView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
            final BlockDialogFragment dialog = new BlockDialogFragment();
            dialog.setTargetFragment(null, 100);
            Bundle bundle = new Bundle();
            bundle.putInt("aaa", 0);
            dialog.setArguments(bundle);
            // dialog.show(getChildFragmentManager(), "my_dialog");
            return true;
        }
    };




    /*****************/
    /*
    // 録画ボタンをクリックしたとき
    @Override
    public void myFunc(Object position) {

        // タッチされた絵コンテ作品のIDを取得
        String storyID[] = dbHelper.getColumn(Table.STORY, Stories.ID.getName());

            FragmentManager fragmentManager = getFragmentManager();

            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // BackStackを設定
                fragmentTransaction.addToBackStack(null);
                int id = Integer.parseInt(position.toString());
                fragmentTransaction.replace(R.id.container,
                            new CameraFragment(dbHelper, Integer.parseInt(storyID[id-1])));
                fragmentTransaction.commit();
            }
    }
    */
}
