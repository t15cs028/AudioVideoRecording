package com.example.storyboard;

import android.view.View;

public interface OnRecyclerListener {
    void onRecyclerClicked(View v, int position);
    void onCameraClicked(View v, int position);
}
