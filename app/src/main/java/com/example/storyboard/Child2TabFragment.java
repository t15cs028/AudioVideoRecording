package com.example.storyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.camera.R;

public class Child2TabFragment extends Fragment {

    static Child2TabFragment newInstance() {return new Child2TabFragment();}
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //addTabの際にBundleを渡す場合は、Bundleから値を取得する
        //渡さない(nullを渡している)場合は、実装しなくてよい。そうでないとgetString("string")時にエラーが発生する
        Bundle args = getArguments();
        String str = args.getString("string");
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.childtag_main, null);
    }
}
