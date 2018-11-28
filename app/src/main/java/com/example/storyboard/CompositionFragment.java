package com.example.storyboard;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.camera.R;
import com.example.database.Composition;
import com.example.database.DBHelper;
import com.example.database.LayoutName;
import com.example.database.Stories;
import com.example.database.Table;
import com.example.database.Tag;

import java.util.ArrayList;
import java.util.List;

public class CompositionFragment extends Fragment {

    private DBHelper dbHelper;
    private int storyBoardNumber;
    private View rootView;

    public static CompositionFragment newInstance(DBHelper dbHelper, int storyBoardNumber){
        CompositionFragment fragment = new CompositionFragment();
        fragment.dbHelper = dbHelper;
        fragment.storyBoardNumber = storyBoardNumber;
        return fragment;
    }
    public CompositionFragment() {

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //FragmentManagerの取得
        FragmentManager mFragmentManager = getChildFragmentManager();
        //xmlからFragmentTabHostを取得、idが android.R.id.tabhost である点に注意
        FragmentTabHost tabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        Log.d("tabHost", String.valueOf(tabHost));
        //ContextとFragmentManagerと、FragmentがあたるViewのidを渡してセットアップ
        tabHost.setup(getActivity().getApplicationContext(), mFragmentManager, R.id.content);
        //String型の引数には任意のidを渡す
        //TabSpecを用意する
        List<TabHost.TabSpec> tabSpec = new ArrayList<>();

        for(int i = 0; i < Tag.values().length; i++){
            tabSpec.add(tabHost.newTabSpec(Tag.values()[i].getName()));
            tabSpec.get(i).setIndicator(Tag.values()[i].getName());
            Tag t = Tag.values()[i];
            Bundle args = new Bundle();
            args.putInt("StoryBoardNumber", storyBoardNumber);
            args.putString("Tag", t.getName());
            tabHost.addTab(tabSpec.get(i), TabPageFragment.class, args);
        }
    }
}
