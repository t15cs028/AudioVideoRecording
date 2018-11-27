package com.example.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

public abstract class BaseCallbackDialog<Interface> extends DialogFragment {



    public enum ListenerType {
        ACTIVITY,
        FRAGMENT,
        OTHER,
    }

    public final static String ARG_LISTENER_TYPE = "listenerType";
    private Interface mListener;

    public void setCallbackListener(Interface listener) {
        mListener = listener;

        // リスナーのタイプをチェック
        ListenerType listenerType;
        if (listener == null) {
            //nullの場合
            listenerType = null;
            setTargetFragment(null, 0);
        } else if (listener instanceof Activity) {
            //Activityの場合
            listenerType = ListenerType.ACTIVITY;
            setTargetFragment(null, 0);
        } else if (listener instanceof Fragment) {
            //Fragmentの場合
            listenerType = ListenerType.FRAGMENT;
            setTargetFragment((Fragment) listener, 0);
        } else {
            //その他の場合
            listenerType = ListenerType.OTHER;
            setTargetFragment(null, 0);
        }

        // 取得したリスナーのタイプをBundleに持たせておく
        /*
        Bundle bundle = getArguments();
        System.out.println("set : " + bundle);
        bundle.putInt(ARG_LISTENER_TYPE, listenerType.ordinal());
        setArguments(bundle);
        */
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ListenerType listenerType = ListenerType.FRAGMENT;

        if (listenerType == ListenerType.ACTIVITY) mListener = (Interface) activity;
        else if (listenerType == ListenerType.FRAGMENT) mListener = (Interface) getTargetFragment();
    }

    public Interface getCallbackListener() {
        return mListener;
    }


}
