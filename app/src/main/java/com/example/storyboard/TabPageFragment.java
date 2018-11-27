package com.example.storyboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.camera.R;
import com.example.database.Composition;
import com.example.database.DBHelper;
import com.example.database.Table;

import java.util.ArrayList;

import static com.example.database.DBHelper.TAG;

public class TabPageFragment extends Fragment {

    private DBHelper dbHelper;
    private int storyBoardNumber;
    private String tag;
    private View rootView;

    static TabPageFragment newInstance(DBHelper dbHelper, int storyBoardNumber, String tag) {
        TabPageFragment fragment = new TabPageFragment();
        fragment.dbHelper = dbHelper;
        fragment.storyBoardNumber = storyBoardNumber;
        fragment.tag = tag;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //addTabの際にBundleを渡す場合は、Bundleから値を取得する
        //渡さない(nullを渡している)場合は、実装しなくてよい。そうでないとgetString("string")時にエラーが発生する
        Bundle args = getArguments();
        dbHelper = (DBHelper) args.getSerializable("DBHelper");
        storyBoardNumber = args.getInt("StoryBoardNumber");
        tag = args.getString("Tag");
        setRetainInstance(true);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.tabpage_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // String id [] = dbHelper.getColumn(Table.COMPOSITION, Composition.ID.getName());
        String name [] = dbHelper.getColumn(
                Table.COMPOSITION, Composition.NAME.getName(),
                Composition.TAG.getName(), tag
        );
        String description [] = dbHelper.getColumn(
                Table.COMPOSITION, Composition.DESCRIPTION.getName(),
                Composition.TAG.getName(), tag
        );
        String thumb_id [] = dbHelper.getColumn(
                Table.COMPOSITION, Composition.THUMB_ID.getName(),
                Composition.TAG.getName(), tag
        );
        String tags [] = dbHelper.getColumn(
                Table.COMPOSITION, Composition.TAG.getName(),
                Composition.TAG.getName(), tag
        );

        if (name != null){
            ArrayList<LayoutBlock> listItem = new ArrayList<>();
            ListView list = (ListView) rootView.findViewById(R.id.layout_listview);


            for(int i = 0; i < name.length; i++) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                        Integer.parseInt(thumb_id[i]));
                LayoutBlock block = new LayoutBlock(i, bmp, name[i], description[i]);
                listItem.add(block);
            }

            LayoutListAdapter adapter =
                    new LayoutListAdapter(getActivity(), R.layout.listlayouts_main, listItem);

            list.setAdapter(adapter);
            list.setOnItemClickListener(onItemClickListener);
        }
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            /*
            // タップしたアイテムの取得
            ListView listView = (ListView)parent;
            Block item = (Block)listView.getItemAtPosition(position);  // SampleListItemにキャスト
            */

            Log.d(TAG, "onListItemClick position => " + position + " : id => " + id);

            // タッチされた絵コンテ作品のIDを取得
            String ids [] = dbHelper.getColumn(
                    Table.COMPOSITION, Composition.ID.getName(),
                    Composition.TAG.getName(), tag
            );
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // BackStackを設定
                fragmentTransaction.addToBackStack(null);
                int layoutID = Integer.parseInt(ids[position]);
                DetailSettingFragment detailSettingFragment
                        = DetailSettingFragment.newInstance(dbHelper, storyBoardNumber, layoutID);

                fragmentTransaction.replace(R.id.container, detailSettingFragment);
                fragmentTransaction.commit();
            }
        }
    };
}
