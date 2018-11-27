package com.example.storyboard;


import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.example.camera.R;
import com.example.database.Composition;
import com.example.database.DBHelper;

public class TabFragment extends Fragment {
    private DBHelper dbHelper;
    private int storyBoardNumber;
    private View rootView;

    // TabHost
    private TabHost mTabHost;
    // Last selected tabId
    private String mLastTabId;

    public void newInstances(DBHelper dbHelpers, int num) {
        this.dbHelper = dbHelpers;
        storyBoardNumber = num;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_main, container, false);
        //FragmentManagerの取得
        FragmentManager mFragmentManager = getChildFragmentManager();
        //xmlからFragmentTabHostを取得、idが android.R.id.tabhost である点に注意
        FragmentTabHost tabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        Log.d("tabHost", String.valueOf(tabHost));
        //ContextとFragmentManagerと、FragmentがあたるViewのidを渡してセットアップ
        tabHost.setup(getActivity().getApplicationContext(), mFragmentManager, R.id.content);
        //String型の引数には任意のidを渡す
        //今回は2つのFragmentをFragmentTabHostから切り替えるため、2つのTabSpecを用意する
        TabHost.TabSpec mTabSpec1 = tabHost.newTabSpec("tab1");
        TabHost.TabSpec mTabSpec2 = tabHost.newTabSpec("tab2");
        //Tab上に表示する文字を渡す
        mTabSpec1.setIndicator("This is tab1");
        mTabSpec2.setIndicator("This is tab2");
        Bundle args = new Bundle();
        args.putString("string", "message");
        //それぞれのTabSpecにclassを対応付けるように引数を渡す
        //第3引数はBundleを持たせることで、Fragmentに値を渡せる。不要である場合はnullを渡す
        tabHost.addTab(mTabSpec1, ChildTabFragment.class, args);
        tabHost.addTab(mTabSpec2, Child2TabFragment.class, args);
        return rootView;
    }

}
