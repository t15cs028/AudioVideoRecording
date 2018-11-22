package com.example.encoder;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.camera.R;
import com.example.database.DBHelper;
import com.example.database.Stories;
import com.example.database.Story;
import com.example.database.Table;

public class MediaPlayFragment extends Fragment {

    private View rootView;
    private DBHelper dbHelper;
    private Table table;
    private int id;

    public MediaPlayFragment(){

    }

    public void newInstances(DBHelper dbHelper, Table table, int id){
        this.dbHelper = dbHelper;
        this.table = table;
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.mediaplay_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final VideoView videoView = (VideoView) rootView.findViewById(R.id.video);

        String url = null;
        switch(table){
            case STORIES:
                url = dbHelper.getColumn(
                        table, Stories.FILE_URL.getName(), Stories.ID.getName(), String.valueOf(id)
                )[0];
                break;
            case STORY:
                url = dbHelper.getColumn(
                        table, Story.FILE_URL.getName(), Story.ID.getName(), String.valueOf(id)
                )[0];
                break;
        }

        videoView.setVideoPath(url);
        final MediaController mController = (MediaController) rootView.findViewById(R.id.controller);
        videoView.setMediaController(new MediaController(getActivity()));
        videoView.start();

        /*
        final MediaController mediaController = (MediaController) rootView.findViewById(R.id.controller);
        mediaController.post(new Runnable() {
            @Override
            public void run() {
                String url = null;
                switch(table){
                    case STORIES:
                        url = dbHelper.getColumn(
                                table, Stories.FILE_URL.getName(), Stories.ID.getName(), String.valueOf(id)
                        )[0];
                        break;
                    case STORY:
                        url = dbHelper.getColumn(
                                table, Story.FILE_URL.getName(), Story.ID.getName(), String.valueOf(id)
                        )[0];
                        break;
                }
                final VideoView videoView = (VideoView) rootView.findViewById(R.id.video);
                videoView.setVideoPath(url);
                mediaController.setAnchorView(videoView);
                videoView.setSoundEffectsEnabled(false);
                videoView.start();
            }
        });
        */
    }


}
