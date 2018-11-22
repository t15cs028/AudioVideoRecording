package com.example.storyboard;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.camera.R;
import com.example.database.Composition;
import com.example.database.DBHelper;
import com.example.database.LayoutName;
import com.example.database.Stories;
import com.example.database.Table;

import java.util.ArrayList;

public class CompositionFragment extends Fragment {

    private DBHelper dbHelper;
    private int storyBoardNumber;
    private View rootView;

    public CompositionFragment() {

    }

    public void newInstances(DBHelper dbHelpers, int num) {
        this.dbHelper = dbHelpers;
        storyBoardNumber = num;
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.composition_main, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // String id [] = dbHelper.getColumn(Table.COMPOSITION, Composition.ID.getName());
        String name [] = dbHelper.getColumn(Table.COMPOSITION, Composition.NAME.getName());
        String description [] = dbHelper.getColumn(Table.COMPOSITION, Composition.DESCRIPTION.getName());
        String thumb_id [] = dbHelper.getColumn(Table.COMPOSITION, Composition.THUMB_ID.getName());




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

            // Log.d(TAG, "onListItemClick position => " + position + " : id => " + id);

            // タッチされた絵コンテ作品のIDを取得
            String ids [] = dbHelper.getColumn(Table.COMPOSITION, Composition.ID.getName());

            FragmentManager fragmentManager = getFragmentManager();

            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // BackStackを設定
                fragmentTransaction.addToBackStack(null);

                DetailBlockFragment detailBlockFragment = new DetailBlockFragment();
                detailBlockFragment.newInstances(dbHelper, storyBoardNumber, Integer.parseInt(ids[position]));
                fragmentTransaction.replace(R.id.container, detailBlockFragment);
                fragmentTransaction.commit();
            }
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        view.findViewById(R.id.lattice).setOnClickListener(onClickListener);
        view.findViewById(R.id.r_person).setOnClickListener(onClickListener);
        view.findViewById(R.id.l_person).setOnClickListener(onClickListener);
        */


        /*
        view.findViewById(R.id.lattice).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View view){
                System.out.println("lattice");
                    int number = dbHelper.getNumOfRecord(DBInformation.stories.ordinal());
                    FragmentManager fragmentManager = getFragmentManager();

                    if (fragmentManager != null) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        // BackStackを設定
                        fragmentTransaction.addToBackStack(null);

                        fragmentTransaction.replace(R.id.container, new DetailBlockFragment(dbHelper, number, "lattice"));
                        fragmentTransaction.commit();
                    }
            }
        });
        */
    }


    /*
        private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            FragmentManager fragmentManager = getFragmentManager();

            if (fragmentManager != null) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // BackStackを設定
                fragmentTransaction.addToBackStack(null);
                switch (view.getId()) {
                    case R.id.lattice:
                        System.out.println("lattice");
                        fragmentTransaction.replace(R.id.container,
                                new DetailBlockFragment(
                                        dbHelper, storyBoardNumber, LayoutName.LATTICE.getLayoutID()));
                        break;
                    case R.id.r_person:
                        System.out.println("r_person");
                        fragmentTransaction.replace(R.id.container,
                                new DetailBlockFragment(
                                        dbHelper, storyBoardNumber, LayoutName.R_PERSON.getLayoutID()));
                        break;
                    case R.id.l_person:
                        System.out.println("l_person");
                        fragmentTransaction.replace(R.id.container,
                                new DetailBlockFragment(
                                        dbHelper, storyBoardNumber, LayoutName.L_PERSON.getLayoutID()));
                        break;
                }
                fragmentTransaction.commit();
            }
        }
    };
    */
}
