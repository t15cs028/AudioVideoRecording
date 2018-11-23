package com.example;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.camera.R;
import com.example.database.DBHelper;
import com.example.storyboard.NewStoryFragment;
import com.example.storyboard.StoryBoardsFragment;



public class TitleFragment extends Fragment {

    private enum Next{
        createStory,
        showStory
    }

    private DBHelper dbHelper;

    public TitleFragment(){

    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        Bundle args = getArguments();
        dbHelper = (DBHelper) args.getSerializable("DBHelper");
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.menu_main, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button [] button = {(Button) view.findViewById(R.id.newstory),
                            (Button) view.findViewById(R.id.story)};

        // button = (Button) view.findViewById(R.id.newstory);
        // Button start = (Button) view.findViewById(R.id.newstory);

        for(int i = 0; i < button.length; i++) {
            Next[] values = Next.values();
            final Next count = values[i];
            // final int count = i;
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager fragmentManager = getFragmentManager();

                    if (fragmentManager != null) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        // BackStackを設定
                        fragmentTransaction.addToBackStack(null);

                        Bundle args = new Bundle();
                        args.putSerializable("DBHelper", dbHelper);
                        switch (count){
                            case createStory:
                                NewStoryFragment newStoryFragment = new NewStoryFragment();
                                newStoryFragment.setArguments(args);
                                fragmentTransaction.replace(R.id.container, newStoryFragment);
                                break;
                            case showStory:
                                StoryBoardsFragment storyBoardsFragment = new StoryBoardsFragment();
                                storyBoardsFragment.setArguments(args);
                                fragmentTransaction.replace(R.id.container, storyBoardsFragment);
                                break;


                        }
                        fragmentTransaction.commit();
                    }
                }
            });
        }
    }

    /*
    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            FragmentManager fragmentManager = getFragmentManager();
            switch (view.getId()) {
                case R.id.newstory:

                    break;
                case R.id.story:
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // BackStackを設定
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.replace(R.id.container, new StoryBoardsFragment());
                    fragmentTransaction.commit();
                    break;
            }
        }
    };
    */



}
